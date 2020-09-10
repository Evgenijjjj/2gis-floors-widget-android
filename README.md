# 2gis-floors-widget-android

Android specific library for [2Gis Floors](http://floors-widget.2gis.ru/)

# Gradle settings

project build.gradle
```groovy

allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
```

module build.gradle
```groovy
dependencies {
	        implementation 'com.github.Evgenijjjj:2gis-floors-widget-android:0.1'
	}
```

# Usage

```xml
<ru.evgenymotorin.gis_floors_widget.GisFloorsView
        android:id="@+id/floors"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:complexId="13933647002593772"
        app:rotatable="true" />
```

Custom attributes

Attribute | Description
:-------------:|:-------------:
complexId | id of the building whose floors are to be shown
initialFirmId | Initializes a widget with an open company card with the passed id
locale | The locale of the widget interface. Available values: RU, ES, EN
hideSearchPanel | Use true if you want the search bar to be invisible
initialZoom | Specifies the zoom level at which to open the widget. If not specified, the scale is selected automatically to fit the entire building in the widget
maxZoom | Specifies the maximum zoom level for the map. If not specified, it is calculated automatically
minZoom | Specifies the minimum zoom level for the map. If not specified, it is calculated automatically
rotatable | Determines if the map with floors can be rotated
initialRotation | Determines the angle of rotation of the map at which to open the widget. Indicated in radians. If not specified, the angle is selected automatically

# Sample

![](samples/sample1.gif)
