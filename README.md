# B-GAN - A multimodal smartphone-based authentication system

Access to the full code documentation can be found [here](https://iberius96.github.io/B-GAN).
Demo paper published from this work: ["Demo: A Multimodal Behavioral Biometric Scheme for Smartphone User Authentication (MBBS)"](https://doi.org/10.1145/3589608.3595083).

---

Behavioural biometrics is the research field concerned with measuring and analysing human behaviour with the purpose of identifying specific patterns that can be used to build unique user profiles.
These patterns, which derive from the way in which a user interacts with a given system, can be used as means for authentication.
Unlike in physiological biometrics, where authentication systems try to capture physical characteristics of the user via dedicated sensors (e.g. fingerprint, iris, etc.), behavioural biometrics authentication systems do not require any additional dedicated hardware in order to gather the data needed to build a given profile.

This work introduces B-GAN, a multimodal smartphone-based user authentication system presented in the form of an Android application. B-GAN uses up to three different input mechanisms (a swipe gesture, a PIN code and a signature) together with additional information related to the user’s holding behaviour in order to gather the behavioural data required to build a set of machine learning models which are used for authentication purposes.
While using B-GAN, a user can provide a set of training interactions which are used by the system to obtain the data necessary to observe its usage patterns.
Given the fact that the application uses this data to train a (set of) one-class classifiers, only positive samples from a genuine user are required in order to build these models. This means that no fraudulent interactions need to be gathered during the training procedure.
Once the active models are successfully built, the same user (or another one impersonating an attacker) can test the accuracy of the system by providing one or more test interactions and observing the responses of the application.
Detailed results relating to a given user and stemming from training and testing the models are stored on the application’s DB and can be saved and easily exported in the form of a set of .csv files.
Such results not only include the performance relating to the generated models, but also granular information on the individual interactions used for building them.
Additionally, the application allows researchers to exonerate specific features and even entire models from the training procedure with the purpose of easily observing how disabling certain sources of information can affect the overall accuracy of the system. 
In the context of the presented application, a generative adversarial network can also be trained and utilised to generate synthetic samples which can contribute to the training procedure.
B-GAN is a stand-alone application that performs all training steps locally using solely the hardware resources provided by the mobile device at hand.
