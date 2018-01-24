# 100DaysofAndroid

## Android fundamentals which I don't understand

### drawable & minimap

The both of drawable and minimap are to store images, but how do you distinguish them?
The point is whether users scale images or not. I worte thier features down below.

#### drawable

* Less memory usage.
* When users scale images, they get rough.

#### minimap

* Lager memory usage.
* Even if users scale images, they look fine.

#### Practice

Minimap is good for app icons, scalable images. Drawable is for the other images.


