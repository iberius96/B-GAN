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

/**
 * The adversarial network.
 * Handles all logic and data related to the GAN.
 * <p>
 * This includes:
 * <ul>
 *     <li>Setting up the architecture of the generator, discriminator and adversarial network (taking into account the currently active features).
 *     <li>Training the network (and its individual components) and generating the synthetic samples.
 * </ul>
 */
public class GAN {

    /**
     * Dimensionality of the latent space vectors.
     */
    private static final int LATENT_DIM = 12;

    /**
     * Number of training epochs.
     */
    public static final int NUM_EPOCHS = 4_000;

    /**
     * Nr of input and/or output units in a hidden layer.
     */
    public static final int NUM_LAYER_UNITS = 8;

    /**
     * Defines the number of base features of an interaction.<p>
     * This includes the Hold and Swipe features (excluding the Swipe segments).
     */
    private int NUM_TRAIN_FEATURES = DatabaseHelper.BASE_FEATURES;

    /**
     * The generator.
     */
    private MultiLayerNetwork generator;

    /**
     * The discriminator
     */
    private MultiLayerNetwork discriminator;

    /**
     * The adversarial network.
     */
    private MultiLayerNetwork gan;

    /**
     * Class constructor.<p>
     * Updates the number of features (NUM_TRAIN_FEATURES) depending on the active models and triggers the initialization of the Generator, Discriminator and GAN objects.
     *
     * @param segments The nr. of Swipe segments.
     * @param pinLength The PIN length.
     * @param signatureSegments The nr. of Signature segments.
     */
    public GAN(Integer segments, Integer pinLength, Integer signatureSegments) {
        this.setSegmentFeatures(segments);
        this.setKeystrokeFeatures(pinLength);
        this.setSignatureFeatures(signatureSegments);
        this.makeGenerator();
        this.makeDiscriminator();
        this.makeGAN();
    }

    /**
     * Builds the set of generator's (hidden) layers.<p>
     * Each layer is a org.deeplearning4j.nn.conf.layers.DenseLayer object which represents a standard fully connected feed forward layer.<br>
     * For each org.deeplearning4j.nn.conf.layers.DenseLayer.Builder we specify:
     * <ul>
     *     <li>The number of inputs (.nIn()).
     *     <li>The Number of outputs (.nOut()).
     *     <li>The Activation function (.activation()).
     *     <li>Weight initialization scheme Distribution (.weightInit()).
     * </ul>
     * The generator is composed by the following three dense layers:
     * <ol>
     *     <li>Nr. of inputs = 12 (LATENT_DIM, which naturally corresponds with the dimensionality of the input latent space vectors), Nr of outputs = 8 (NUM_LAYER_UNITS), activation function = ReLU (Activation.RELU), weight initialization = ReLU uniform (He et al. (2015), "Delving Deep into Rectifiers". Uniform distribution U(-s,s) with s = sqrt(6/fanIn)).
     *     <li>Nr. of inputs = 8 (NUM_LAYER_UNITS), Nr of outputs = 8 (NUM_LAYER_UNITS), activation function = ReLU (Activation.RELU), weight initialization = Xavier uniform (As per Glorot and Bengio 2010: Uniform distribution U(-s,s) with s = sqrt(6/(fanIn + fanOut))).
     *     <li>Nr. of inputs = 8 (NUM_LAYER_UNITS), Nr of outputs = NUM_TRAIN_FEATURES (corresponding to the active features), activation function = Sigmoid (Activation.SIGMOID), weight initialization = Xavier uniform.
     * </ol>
     *
     * @return The set of (hidden) layers of the generator.
     */
    private Layer[] getGeneratorLayers() {
        return new Layer[]{
                new DenseLayer.Builder()
                        .nIn(LATENT_DIM)
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
                        .nOut(NUM_TRAIN_FEATURES)
                        .weightInit(WeightInit.XAVIER_UNIFORM)
                        .activation(Activation.SIGMOID)
                        .build()
        };
    }

    /**
     * Initializes the org.deeplearning4j.nn.multilayer.MultiLayerNetwork object that represents the neural network making up the generator.<p>
     * The configuration of the generator's network is expressed by a org.deeplearning4j.nn.conf.MultiLayerConfiguration object obtained by building a org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder object.<br>
     * The ListBuilder is obtained by calling the .list() method of a org.deeplearning4j.nn.conf.NeuralNetConfiguration.Builder object and passing as parameter the set of dense layers returned from the .getGeneratorLayers() method.<br>
     * The Builder object is initialised with a org.nd4j.linalg.learning.config.Adam gradient updater (http://arxiv.org/abs/1412.6980) with epsilon value = 0.0000001.
     */
    private void makeGenerator() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(Adam.builder().epsilon(0.0000001).build())
                .list(this.getGeneratorLayers())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        this.generator = network;
    }

    /**
     * Builds the set of discriminator's (hidden) layers.<p>
     * Each layer is a org.deeplearning4j.nn.conf.layers.DenseLayer object which represents a standard fully connected feed forward layer.<br>
     * For each org.deeplearning4j.nn.conf.layers.DenseLayer.Builder we specify:
     * <ul>
     *     <li>The number of inputs (.nIn()).
     *     <li>The Number of outputs (.nOut()).
     *     <li>The Activation function (.activation()).
     *     <li>Weight initialization scheme Distribution (.weightInit()).
     * </ul>
     * Additionally, the discriminator has a final org.deeplearning4j.nn.conf.layers.LossLayer representing an output layer without parameters (having only a loss function and an activation function).<br>
     * The discriminator is composed by the following three dense layers:
     * <ol>
     *     <li>Nr. of inputs = NUM_TRAIN_FEATURES (corresponding to the active features), Nr of outputs = 8 (NUM_LAYER_UNITS), activation function = ReLU (Activation.RELU), weight initialization = ReLU uniform (He et al. (2015), "Delving Deep into Rectifiers". Uniform distribution U(-s,s) with s = sqrt(6/fanIn)).
     *     <li>Nr. of inputs = 8 (NUM_LAYER_UNITS), Nr of outputs = 8 (NUM_LAYER_UNITS), activation function = ReLU (Activation.RELU), weight initialization = Xavier uniform (As per Glorot and Bengio 2010: Uniform distribution U(-s,s) with s = sqrt(6/(fanIn + fanOut))).
     *     <li>Nr. of inputs = 8 (NUM_LAYER_UNITS), Nr of outputs = 1, activation function = Sigmoid (Activation.SIGMOID), weight initialization = Xavier uniform.
     * </ol>
     * And the following loss layer:
     * <ol>
     *     <li>Loss function = Cross entropy (LossFunctions.LossFunction.XENT), activation function = Linear (Activation.IDENTITY), weight initialization = Xavier uniform.
     * </ol>
     *
     * @return The set of (hidden) layers of the discriminator.
     */
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

    /**
     * Initializes the org.deeplearning4j.nn.multilayer.MultiLayerNetwork object that represents the neural network making up the discriminator.<p>
     * The configuration of the generator's network is expressed by a org.deeplearning4j.nn.conf.MultiLayerConfiguration object obtained by building a org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder object.<br>
     * The ListBuilder is obtained by calling the .list() method of a org.deeplearning4j.nn.conf.NeuralNetConfiguration.Builder object and passing as parameter the set of dense layers returned from the .getDiscriminatorLayers() method.<br>
     * The Builder object is initialised with a org.nd4j.linalg.learning.config.Adam gradient updater (http://arxiv.org/abs/1412.6980) with epsilon value = 0.0000001.
     */
    private void makeDiscriminator() {
        MultiLayerConfiguration conf = new NeuralNetConfiguration.Builder()
                .updater(Adam.builder().epsilon(0.0000001).build())
                .list(this.getDiscriminatorLayers())
                .build();

        MultiLayerNetwork network = new MultiLayerNetwork(conf);
        network.init();

        this.discriminator = network;
    }

    /**
     * Initializes the org.deeplearning4j.nn.multilayer.MultiLayerNetwork object that represents the neural network making up the full GAN.<p>
     * The configuration of the GAN is expressed by a org.deeplearning4j.nn.conf.MultiLayerConfiguration object obtained by building a org.deeplearning4j.nn.conf.NeuralNetConfiguration.ListBuilder object.<br>
     * The ListBuilder is obtained by calling the .list() method of a org.deeplearning4j.nn.conf.NeuralNetConfiguration.Builder object and passing as parameter the set of dense layers returned from both the .getGeneratorLayers() and the .getDiscriminatorLayers() methods.<br>
     * The Builder object is initialised with a org.nd4j.linalg.learning.config.Adam gradient updater (http://arxiv.org/abs/1412.6980) with epsilon value = 0.0000001.<p>
     * The parameters of the discriminator's layers are frozen (initialized as org.deeplearning4j.nn.layers.FrozenLayerWithBackprop) since we don't update them when fitting the GAN (instead, we use the .updateGan() method to update them from the discriminator object).
     */
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

    /**
     * Updates the generator using the generator's layers of the GAN after the last training epoch has been concluded.
     */
    private void updateGen() {
        for (int i = 0; i < this.generator.getLayers().length; i++) {
            this.generator.getLayer(i).setParams(this.gan.getLayer(i).params());
        }
    }

    /**
     * Updates the discriminator's layers of the GAN after each discriminator's fit.
     */
    private void updateGan() {
        int genLayerCount = this.generator.getLayers().length;
        for (int i = genLayerCount; i < this.gan.getLayers().length; i++) {
            this.gan.getLayer(i).setParams(this.discriminator.getLayer(i - genLayerCount).params());
        }
    }

    /**
     * Generates the synthetic samples starting from a set of genuine ones.<p>
     * As a first step, the method performs min/max scaling on all (genuine) input interactions and uses the normalized samples to initialise a org.deeplearning4j.datasets.iterator.DoublesDataSetIterator (representing the GAN training set).<br>
     * Then, a set of random vectors is sampled from the latent space.
     * <ul>
     *     <li>The nr. of generated vectors is equal to the nr. of genuine samples.
     *     <li>The dimensionality of each vector is determined by LATENT_DIM.
     * </ul>
     * The random vectors are fed as input to the GAN's generator layers which create a set of synthetic examples.<br>
     * Real and fake datasets (marked as 1 and 0 respectively) are created and merged.<br>
     * The method then fits the discriminator on a combined batch of real and fake samples and updates the GAN's discriminator layers by calling the .updateGAN() method.<br>
     * A set of adversarial examples is then generated and labeled as genuine (with the purpose of misleading the discriminator) and then used to fit the GAN.<br>
     * By labeling this generated samples as genuine, we reward the generator for successfully fooling the discriminator.<p>
     * The process is repeated for the number of specified epochs (NUM_EPOCHS).<br>
     * Once the last training epoch has been concluded, the generator is updated using the generator's layers of the GAN (.updateGen() method).<br>
     * Finally, the generator is used to output the specified number of synthetic examples.<br>
     * Each synthetic example is de-normalized using the Swipe.fromNormalizedValues() method.
     *
     * @param realSwipes The genuine interactions.
     * @param numberFakeSamples The nr. of synthetic samples the GAN needs to generate.
     * @param progressTextView The TextView used to display the training progress.
     * @return The set of synthetic interactions.
     * @throws Exception The thrown exception.
     */
    public ArrayList<Swipe> getFakeSwipeSamples(ArrayList<Swipe> realSwipes, int numberFakeSamples, TextView progressTextView) throws Exception {

        List<Pair<double[], double[]>> realSwipesPairs = new ArrayList<Pair<double[], double[]>>();
        for(Swipe swipe: realSwipes){
            Pair<double[], double[]> record = new Pair<double[], double[]>(swipe.getNormalizedValues(realSwipes), new double[]{0});
            realSwipesPairs.add(record);
        }

        int batchSize = realSwipesPairs.size();
        DoublesDataSetIterator trainData = new DoublesDataSetIterator(realSwipesPairs, batchSize);
        trainData.reset(); // Resets the iterator back to the beginning.

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

                this.updateGan();

                DataSet adversarialSet = new DataSet(Nd4j.rand(batchSize, LATENT_DIM), Nd4j.ones(batchSize, 1));
                this.gan.fit(adversarialSet);

            }
            trainData.reset();
        }

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

    /**
     * Increments the number of features (NUM_TRAIN_FEATURES) based on the active Swipe segments.<p>
     * In this context, each segment (value) is considered as a separate feature.<br>
     * Additionally, for each segment an an X and Y value is stored for each interaction.
     *
     * @param segmentFeatures The nr of Swipe segments.
     */
    public void setSegmentFeatures(Integer segmentFeatures) {
        this.NUM_TRAIN_FEATURES += (segmentFeatures * 2);
    }

    /**
     * Increments the number of features (NUM_TRAIN_FEATURES) based on the active Keystroke attributes.
     *
     * @param pinLength The length of the PIN sequence.
     */
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

    /**
     * Increments the number of features (NUM_TRAIN_FEATURES) based on the active Signature attributes.
     *
     * @param segmentFeatures The nr of Signature segments.
     */
    public void setSignatureFeatures(Integer segmentFeatures) {
        for(String signature_feature : DatabaseHelper.signature_features) {
            if(segmentFeatures != 0) {
                switch (signature_feature) {
                    case DatabaseHelper.COL_SIGNATURE_SEGMENTS_X:
                    case DatabaseHelper.COL_SIGNATURE_SEGMENTS_Y:
                        this.NUM_TRAIN_FEATURES += segmentFeatures;
                        break;
                    default:
                        this.NUM_TRAIN_FEATURES += 1;
                        break;
                }
            }
        }
    }
}
