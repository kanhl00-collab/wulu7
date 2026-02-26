from tensorflow.keras import layers, models, applications, optimizers
import tensorflow_datasets as tfds
import tensorflow as tf
import matplotlib.pyplot as plt

#load dataset
data, info = tfds.load("citrus_leaves", with_info=True)
#get number of classes
n_classes = info.features["label"].num_classes

# split into train, test, and validation dataset
(x_train, y_train), (x_test, y_test) = tfds.as_numpy(
    tfds.load("citrus_leaves",  split=["train[0%:75%]", "train[75%:]"], batch_size=-1, as_supervised=True))

x_train, x_test = x_train/255.0, x_test/255.0
#hand crafted CNN
model = tf.keras.Sequential([
  tf.keras.layers.Conv2D(32, 3, activation='relu',input_shape = (256,256,3)),
  tf.keras.layers.MaxPooling2D(),
  tf.keras.layers.Conv2D(64, 3, activation='relu'),
  tf.keras.layers.MaxPooling2D(),
  tf.keras.layers.Conv2D(128, 3, activation='relu'),
  tf.keras.layers.MaxPooling2D(),
  tf.keras.layers.Flatten(),
  tf.keras.layers.Dense(128, activation='relu'),
  tf.keras.layers.Dense(n_classes, activation = 'softmax')
])

model.compile(optimizer="adam",
              loss = "sparse_categorical_crossentropy",
              metrics =['accuracy'])

history = model.fit(x_train, y_train, epochs=15, validation_data = (x_test, y_test), batch_size =64)
#transfer learning from vgg16
base_model = applications.VGG16(input_shape=(256,256,3),
                                      include_top = False, 
                                      weights = "imagenet")

base_model.trainable = False

for layer in base_model.layers[-2:]:
    layer.trainable = True

new_model = models.Sequential([
    base_model,
    layers.Flatten(),
    layers.Dense(128, activation = "relu"),
    layers.Dropout(0.2),
    layers.Dense(64, activation = "relu"),
    layers.Dropout(0.2),
    layers.Dense(n_classes, activation = "softmax")
])

#new_model.summary()

adam_low_rate = optimizers.Adam(learning_rate = 0.001)

new_model.compile(optimizer = adam_low_rate,
                  loss = "sparse_categorical_crossentropy",
                  metrics = ['accuracy'])

new_history = new_model.fit(x_train, y_train, epochs = 15, validation_data = (x_test, y_test), batch_size=64)
#plot result
#this is basically the same code for my week 12 assignment
plt.subplot(1,2,1)
plt.plot(history.history['accuracy'], '^--r',label = "accuracy-CNN")
plt.plot(new_history.history['accuracy'], 'o-.g',label = "accuracy-TL")

plt.plot(history.history['val_accuracy'], '*-m',label = "val_accuracy-CNN")
plt.plot(new_history.history['val_accuracy'], 'x-b',label = "val_accuracy-TL")

plt.title("Accuracy comparison")
plt.xlabel("Epoch")
plt.ylabel("Accuracy")

plt.legend(loc="best")
plt.grid()
plt.subplot(1,2,2)
plt.plot(history.history['loss'], '^--r',label = "loss-CNN")
plt.plot(new_history.history['loss'], 'o-.g',label = "loss-TL")

plt.plot(history.history['val_loss'], '*-m',label = "val_loss-CNN")
plt.plot(new_history.history['val_loss'], 'x-b',label = "val_loss-TL")

plt.title("Loss comparison")
plt.xlabel("Epoch")
plt.ylabel("Loss")

plt.legend(loc="best")
plt.grid()
plt.suptitle("CNN vs Transfer Learning")

plt.show()