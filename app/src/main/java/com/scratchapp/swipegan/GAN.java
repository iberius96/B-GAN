package com.scratchapp.swipegan;

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
import org.nd4j.linalg.activations.impl.ActivationReLU;
import org.nd4j.linalg.api.ndarray.INDArray;
import org.nd4j.linalg.dataset.DataSet;
import org.nd4j.linalg.factory.Nd4j;
import org.nd4j.linalg.learning.config.Adam;
import org.nd4j.linalg.learning.config.IUpdater;
import org.nd4j.linalg.lossfunctions.LossFunctions;
import org.nd4j.linalg.primitives.Pair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GAN {
    private static final double LEARNING_RATE = 0.0002;
    private static final double GRADIENT_THRESHOLD = 100.0;
    private static final IUpdater UPDATER = Adam.builder().learningRate(LEARNING_RATE).beta1(0.5).build();

    private static final int BATCH_SIZE = 5;
    private static final int LATENT_DIM = 100;
    private static final int NUM_EPOCHS = 10_000;

    private MultiLayerNetwork generator;
    private MultiLayerNetwork discriminator;
    private MultiLayerNetwork gan;

    public GAN() {
        this.makeGenerator();
        this.makeDiscriminator();
        this.makeGAN();
    }

    private Layer[] getGeneratorLayers() {
        return new Layer[]{
                new DenseLayer.Builder().nIn(100).nOut(128).weightInit(WeightInit.UNIFORM).build(),
                new ActivationLayer.Builder(new ActivationReLU()).build(),
                new DenseLayer.Builder().nIn(128).nOut(64).build(),
                new ActivationLayer.Builder(new ActivationReLU()).build(),
                new DenseLayer.Builder().nIn(64).nOut(33).activation(Activation.SIGMOID).build()
        };
    }

    private void makeGenerator() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(42)
                .updater(UPDATER)
//                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
//                .gradientNormalizationThreshold(GRADIENT_THRESHOLD)
//                .weightInit(WeightInit.XAVIER)
//                .activation(Activation.RELU)
                .list(getGeneratorLayers())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        this.generator = network;
    }

    private Layer[] getDiscriminatorLayers() {
        return new Layer[]{
                new DenseLayer.Builder().nIn(33).nOut(128).weightInit(WeightInit.UNIFORM).build(),
                new ActivationLayer.Builder(new ActivationReLU()).build(),
                new DenseLayer.Builder().nIn(128).nOut(64).build(),
                new ActivationLayer.Builder(new ActivationReLU()).build(),
                new OutputLayer.Builder(LossFunctions.LossFunction.XENT).nIn(64).nOut(1).activation(Activation.SIGMOID).build()
        };
    }

    private void makeDiscriminator() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(42)
                .updater(UPDATER)
//                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
//                .gradientNormalizationThreshold(GRADIENT_THRESHOLD)
//                .weightInit(WeightInit.XAVIER)
//                .activation(Activation.IDENTITY)
                .list(getDiscriminatorLayers())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        this.discriminator = network;
    }

    private void makeGAN() {
        Layer[] genLayers = this.getGeneratorLayers();
        Layer[] disLayers = Arrays.stream(this.getDiscriminatorLayers())
                .map((layer) -> {
                    if (layer instanceof DenseLayer || layer instanceof OutputLayer) {
                        return new FrozenLayerWithBackprop(layer);
                    } else {
                        return layer;
                    }
                }).toArray(Layer[]::new);
        Layer[] layers = ArrayUtils.addAll(genLayers, disLayers);

        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .seed(42)
                .updater(UPDATER)
//                .gradientNormalization(GradientNormalization.RenormalizeL2PerLayer)
//                .gradientNormalizationThreshold(GRADIENT_THRESHOLD)
//                .weightInit(WeightInit.XAVIER)
//                .activation(Activation.IDENTITY)
                .list(layers)
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        this.gan = network;
    }

    private void copyParamsFromGAN() {
        int genLayerCount = this.generator.getLayers().length;
        for (int i = 0; i < this.gan.getLayers().length; i++) {
            if (i < genLayerCount) {
                this.generator.getLayer(i).setParams(this.gan.getLayer(i).params());
            } else {
                this.discriminator.getLayer(i - genLayerCount).setParams(this.gan.getLayer(i).params());
            }
        }
    }

    private void updateGen() {
        for (int i = 0; i < this.generator.getLayers().length; i++) {
            this.generator.getLayer(i).setParams(gan.getLayer(i).params());
        }
    }

    private void updateGan() {
        int genLayerCount = this.generator.getLayers().length;
        for (int i = genLayerCount; i < gan.getLayers().length; i++) {
            gan.getLayer(i).setParams(this.discriminator.getLayer(i - genLayerCount).params());
        }
    }

    public ArrayList<Swipe> getFakeSwipeSamples(ArrayList<Swipe> realSwipes, int numberFakeSamples, TextView progressTextView) throws Exception {

        List<Pair<double[], double[]>> realSwipesPairs = new ArrayList<Pair<double[], double[]>>();
        for(Swipe swipe: realSwipes){
            Pair<double[], double[]> record = new Pair<double[], double[]>(swipe.getNormalizedValues(), new double[]{0});
            realSwipesPairs.add(record);
        }

        DoublesDataSetIterator trainData = new DoublesDataSetIterator(realSwipesPairs, BATCH_SIZE);
        this.copyParamsFromGAN();
        trainData.reset();

        for (int i = 0; i <= NUM_EPOCHS; i++) {
            if (i % 100 == 0) {
                int epoch = i;
                Handler uiHandler = new Handler(Looper.getMainLooper());
                uiHandler.post(new Runnable() {
                    public void run() {
                        progressTextView.setText("GAN epoch: " + epoch + " (out of 10_000)");
                    }
                });
            }

            while (trainData.hasNext()) {

                // generate data
                INDArray real = trainData.next().getFeatures();
                int batchSize = (int) real.shape()[0];

                INDArray fakeIn = Nd4j.rand(batchSize, LATENT_DIM);
                INDArray fake = gan.activateSelectedLayers(0, this.generator.getLayers().length - 1, fakeIn);

                DataSet realSet = new DataSet(real, Nd4j.ones(batchSize, 1));
                DataSet fakeSet = new DataSet(fake, Nd4j.zeros(batchSize, 1));

                List<DataSet> mergeDataset = new ArrayList<DataSet>();
                mergeDataset.add(realSet);
                mergeDataset.add(fakeSet);

                DataSet data = DataSet.merge(mergeDataset);

                this.discriminator.fit(data);
                this.discriminator.fit(data);

                // Update the discriminator in the GAN network
                this.updateGan();

                gan.fit(new DataSet(Nd4j.rand(batchSize, LATENT_DIM), Nd4j.ones(batchSize, 1)));

                // after 5_000 epochs we start to extract 5 samples after each successive 500 epochs
//                if ((i >= 5_000) && (i % 500 == 0)) {
//                    // Copy the GANs generator to gen.
//                    updateGen(gen, gan);
//
//                    for (int j = 0; j < 5; j++) {
//                        fakeSwipes.add(new Swipe(gen.output(Nd4j.rand(1, LATENT_DIM)).toDoubleVector()));
//                    }
//                }

            }
            trainData.reset();
        }

        // Copy the GANs generator to gen.
        this.updateGen();

        //gen.save(new File("mnist-mlp-generator.dlj"));
        ArrayList<Swipe> fakeSwipes = new ArrayList<Swipe>();
        for (int i = 0; i < numberFakeSamples; i++) {
            fakeSwipes.add(Swipe.fromNormalizedValues(this.generator.output(Nd4j.rand(1, LATENT_DIM)).toDoubleVector()));
        }
        return fakeSwipes;
    }
}
