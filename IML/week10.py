from tensorflow.keras import datasets, layers, models, applications, optimizers

base_model = applications.VGG16(input_shape=(32,32,3),
                                      include_top = False, 
                                      weights = "imagenet")

(x_train, y_train), (x_test, y_test) = datasets.cifar10.load_data()

#should not miss this line otherwise accuracy is low
x_train, x_test = x_train/255.0, x_test/255.0

base_model.trainable = False

for layer in base_model.layers[-2:]:
    layer.trainable = True

new_model = models.Sequential([
    base_model,
    layers.Flatten(),
    layers.Dense(64, activation = "relu"),
    layers.Dense(32, activation = "relu"),
    layers.Dropout(0.2),
    layers.Dense(10, activation = "softmax")
])

new_model.summary()

adam_low_rate = optimizers.Adam(learning_rate = 0.001)

new_model.compile(optimizer = adam_low_rate,
                  loss = "sparse_categorical_crossentropy",
                  metrics = ['accuracy'])

history = new_model.fit(x_train, y_train, epochs = 5, validation_data = (x_test, y_test), batch_size=64)

