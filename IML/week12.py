import tensorflow as tf 
import matplotlib.pyplot as plt 
mnist = tf.keras.datasets.mnist

(x_train, y_train), (x_test, y_test) = mnist.load_data()
x_train, x_test = x_train/255.0, x_test/255.0

model1 = tf.keras.models.Sequential([
    tf.keras.layers.Flatten(input_shape=(28,28)),
    tf.keras.layers.Dense(128, activation = "relu"),
    tf.keras.layers.Dense(64, activation = "relu"),
    tf.keras.layers.Dense(32, activation = "relu"),
    tf.keras.layers.Dense(10, activation = "softmax"),
])

model2 = tf.keras.models.Sequential([
    tf.keras.layers.Conv2D(32, (3,3), strides = (1,1), activation = "relu", input_shape = (28,28,1)),
    tf.keras.layers.MaxPooling2D((2,2)),
    tf.keras.layers.Conv2D(64, (3,3), activation = "relu"),
    tf.keras.layers.MaxPooling2D((2,2)),
    tf.keras.layers.Conv2D(128, (3,3), activation = "relu"),
    tf.keras.layers.MaxPooling2D((2,2)),
    tf.keras.layers.Flatten(),
    tf.keras.layers.Dense(10, activation = "softmax")
])

model1.compile(optimizer="adam",
              loss = "sparse_categorical_crossentropy",
              metrics =['accuracy'])

history1 = model1.fit(x_train, y_train, epochs=10, validation_data = (x_test, y_test), batch_size =64)

model2.compile(optimizer="adam",
              loss = "sparse_categorical_crossentropy",
              metrics =['accuracy'])

history2 = model2.fit(x_train, y_train, epochs=10, validation_data = (x_test, y_test), batch_size =64)

plt.subplot(1,2,1)
plt.plot(history1.history['accuracy'], '^--r',label = "accuracy-DNN")
plt.plot(history2.history['accuracy'], 'o-.g',label = "accuracy-CNN")

plt.plot(history1.history['val_accuracy'], '*-m',label = "val_accuracy-DNN")
plt.plot(history2.history['val_accuracy'], 'x-b',label = "val_accuracy-CNN")

plt.title("Accuracy comparison")
plt.xlabel("Epoch")
plt.ylabel("Accuracy")

plt.legend(loc="best")
plt.grid()
plt.subplot(1,2,2)
plt.plot(history1.history['loss'], '^--r',label = "loss-DNN")
plt.plot(history2.history['loss'], 'o-.g',label = "loss-CNN")

plt.plot(history1.history['val_loss'], '*-m',label = "val_loss-DNN")
plt.plot(history2.history['val_loss'], 'x-b',label = "val_loss-CNN")

plt.title("Loss comparison")
plt.xlabel("Epoch")
plt.ylabel("Loss")

plt.legend(loc="best")
plt.grid()
plt.suptitle("DNN vs CNN")

plt.show()