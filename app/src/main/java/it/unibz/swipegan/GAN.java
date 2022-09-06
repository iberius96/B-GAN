package it.unibz.swipegan;

import android.os.Handler;
import android.os.Looper;
import android.widget.TextView;

import org.apache.commons.lang3.ArrayUtils;
import org.deeplearning4j.datasets.iterator.DoublesDataSetIterator;
import org.deeplearning4j.nn.conf.MultiLayerConfiguration;
import org.deeplearning4j.nn.conf.NeuralNetConfiguration;
import org.deeplearning4j.nn.conf.layers.*;
import org.deeplearning4j.nn.conf.layers.misc.FrozenLayerWithBackprop;
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork;
import org.deeplearning4j.nn.weights.WeightInit;
import org.nd4j.linalg.activations.Activation;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.primitives.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GAN {

    private static final int LATENT_DIM = 12;
    public static final int NUM_EPOCHS = 4_000;
    public static final int NUM_LAYER_UNITS = 8;
    private int NUM_TRAIN_FEATURES = DatabaseHelper.BASE_FEATURES;

    private MultiLayerNetwork generator;
    private MultiLayerNetwork discriminator;
    private MultiLayerNetwork gan;

    public GAN(Integer segments, Integer pinLength) {
        this.setSegmentFeatures(segments);
        this.setKeystrokeFeatures(pinLength);
        this.makeGenerator();
        this.makeDiscriminator();
        this.makeGAN();
    }

    private Layer[] getGeneratorLayers() {
        return new Layer[]{
                new DenseLayer.Builder()
                        .nIn(LATENT_DIM)
                        .nOut(NUM_LAYER_UNITS) // try 64
                        .weightInit(WeightInit.RELU_UNIFORM)
                        .activation(Activation.RELU)
                        .build(),
                new DenseLayer.Builder()
                        .nIn(NUM_LAYER_UNITS) //try 64
                        .nOut(NUM_LAYER_UNITS) //try 128
                        .weightInit(WeightInit.XAVIER_UNIFORM)
                        .activation(Activation.RELU)
                        .build(),
                new DenseLayer.Builder()
                        .nIn(NUM_LAYER_UNITS) //try 128
                        .nOut(NUM_TRAIN_FEATURES)
                        .weightInit(WeightInit.XAVIER_UNIFORM)
                        .activation(Activation.SIGMOID)
                        .build()
        };
    }

    private void makeGenerator() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(Adam.builder().epsilon(0.0000001).build())
                .list(this.getGeneratorLayers())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        this.generator = network;
    }

    private Layer[] getDiscriminatorLayers() {
        return new Layer[]{
                new DenseLayer.Builder()
                        .nIn(NUM_TRAIN_FEATURES)
                        .nOut(NUM_LAYER_UNITS)
                        .weightInit(WeightInit.RELU_UNIFORM)
                        .activation(Activation.RELU)
                        .build(),
                new DenseLayer.Builder()
                        .nIn(NUM_LAYER_UNITS)
                        .nOut(NUM_LAYER_UNITS)
                        .weightInit(WeightInit.XAVIER_UNIFORM)
                        .activation(Activation.RELU)
                        .build(),
                new DenseLayer.Builder()
                        .nIn(NUM_LAYER_UNITS)
                        .nOut(1)
                        .weightInit(WeightInit.XAVIER_UNIFORM)
                        .activation(Activation.SIGMOID)
                        .build(),
                new LossLayer.Builder(LossFunctions.LossFunction.XENT)
                        .weightInit(WeightInit.XAVIER)
                        .activation(Activation.IDENTITY)
                        .build()
        };
    }

    private void makeDiscriminator() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(Adam.builder().epsilon(0.0000001).build())
                .list(this.getDiscriminatorLayers())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        this.discriminator = network;
    }

    private void makeGAN() {
        Layer[] genLayers = this.getGeneratorLayers();
        Layer[] disLayers = Arrays.stream(this.getDiscriminatorLayers())
                .map((layer) -> {
                    return new FrozenLayerWithBackprop(layer);
                }).toArray(Layer[]::new);
        Layer[] layers = ArrayUtils.addAll(genLayers, disLayers);

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(Adam.builder().epsilon(0.0000001).build())
                .list(layers)
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        this.gan = network;
    }

    private void updateGen() {
        for (int i = 0; i < this.generator.getLayers().length; i++) {
            this.generator.getLayer(i).setParams(this.gan.getLayer(i).params());
        }
    }

    private void updateGan() {
        int genLayerCount = this.generator.getLayers().length;
        for (int i = genLayerCount; i < this.gan.getLayers().length; i++) {
            this.gan.getLayer(i).setParams(this.discriminator.getLayer(i - genLayerCount).params());
        }
    }

    public ArrayList<Swipe> getFakeSwipeSamples(ArrayList<Swipe> realSwipes, int numberFakeSamples, TextView progressTextView) throws Exception {

        List<Pair<double[], double[]>> realSwipesPairs = new ArrayList<Pair<double[], double[]>>();
        for(Swipe swipe: realSwipes){
            Pair<double[], double[]> record = new Pair<double[], double[]>(swipe.getNormalizedValues(realSwipes), new double[]{0});
            realSwipesPairs.add(record);
        }

        int batchSize = realSwipesPairs.size();
        DoublesDataSetIterator trainData = new DoublesDataSetIterator(realSwipesPairs, batchSize);
        trainData.reset();

        for (int i = 0; i <= NUM_EPOCHS; i++) {
            if (i % 100 == 0) {
                int epoch = i;
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    public void run() {
                        progressTextView.setText("GAN epoch: " + epoch + " (out of " + NUM_EPOCHS + ")");
                    }
                });
            }
            while (trainData.hasNext()) {
                // generate data
                INDArray real = trainData.next().getFeatures();

                INDArray fakeIn = Nd4j.rand(batchSize, LATENT_DIM);
                INDArray fake = this.gan.activateSelectedLayers(0, this.generator.getLayers().length - 1, fakeIn);

                DataSet realSet = new DataSet(real, Nd4j.ones(batchSize, 1));
                DataSet fakeSet = new DataSet(fake, Nd4j.zeros(batchSize, 1));

                List<DataSet> mergeDataset = new ArrayList<DataSet>();
                mergeDataset.add(realSet);
                mergeDataset.add(fakeSet);
                DataSet data = DataSet.merge(mergeDataset);
                this.discriminator.fit(data);

                // Update the discriminator in the GAN network
                this.updateGan();
                this.gan.fit(new DataSet(Nd4j.rand(batchSize, LATENT_DIM), Nd4j.ones(batchSize, 1)));

            }
            trainData.reset();
        }

        // Copy the GANs generator to gen.
        this.updateGen();
        System.out.println("fakeSwipeValues------------------------------------------------------------------");

        ArrayList<Swipe> fakeSwipes = new ArrayList<Swipe>();
        for (int i = 0; i < numberFakeSamples; i++) {
            double[] fakeSwipeValues;
            fakeSwipeValues = this.generator.output(Nd4j.rand(1, LATENT_DIM)).toDoubleVector();
            fakeSwipes.add(Swipe.fromNormalizedValues(fakeSwipeValues, 0, "User", realSwipes));
            System.out.println(Arrays.toString(fakeSwipeValues));
        }

        return fakeSwipes;
    }

    public void setSegmentFeatures(Integer segmentFeatures) {
        this.NUM_TRAIN_FEATURES += (segmentFeatures * 2); // Increment for both X and Y axis
    }

    public void setKeystrokeFeatures(Integer pinLength) {
        if(pinLength != 0) {
            for (String keystroke_feature : DatabaseHelper.keystroke_features) {
                switch (keystroke_feature) {
                    case DatabaseHelper.COL_KEYSTROKE_FULL_DURATION:
                        this.NUM_TRAIN_FEATURES += 1;
                        break;
                    case DatabaseHelper.COL_KEYSTROKE_DURATIONS:
                        this.NUM_TRAIN_FEATURES += pinLength;
                        break;
                    default:
                        this.NUM_TRAIN_FEATURES += (pinLength - 1);
                        break;
                }
            }
        }
    }
}
