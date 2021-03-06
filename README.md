![#DyNaDeMo](screens/02.png)
*Dynamic Narrative Modelling Demo*

This is a techo demo created to demonostrate
*[DyNaMo](https://github.com/EchoCam/DynamicNarrative)*, a set of tools to help
game developers develop, debug and use multinarrative stories.

Built using the [jMonkeyEngine](http://jmonkeyengine.org/) and DyNaMo.

![#DyNaDeMo](screens/01.png)

Building, Running and Testing
=============================

At the moment, to build, run and test the package, open the DyNaDeMo folder
using the [jMonkeyEngine3 SDK](http://jmonkeyengine.org/downloads/).


Building Mesh Data From Blender
=============================

With JMonkeyEngine SDK
----------------------

Save the .blend file to the DyNaDeMo/assets/Textures folder along with any
dependant files. (Note texture paths may need to be updated). Right click the
.blend file and click "convert to j3o". Move the created file to th
DyNaDeMo/assets/Scenes folder and make sure that file is loaded correctly by
the asset manager.

With Eclipse
--------------

Installing OGRE exporter:

Download the correct version of
[blender2ogre](https://code.google.com/archive/p/blender2ogre/downloads) and
extract the contained .py file. In blender, go to file->"User Preferences.." and
click "install from file..". Select to the .py file and make sure the OGRE
exporter addon is selected, then hit save user preferences.

Exporting mesh data:

To export the data, select all objects you wish to export then go to
file->Export->Ogre3d. Navigate to the DyNaDeMo/assets/Textures folder and export
the various files. Move the .scene file to the DyNaDeMo/assets/Scenes folder and
make sure that the file is loaded correctly by the asset manager.

Special Thanks
==============

 - To the [Pyland project](https://github.com/pyland/pyland)  and
   [Alex Day](https://github.com/alexandaday) for making assume music available
   under a permissive license!

 Game code, art assets and design © 2016 James Riordan, Angus Hammond, Elise
 Xue, Robin McFarland, Tim Ringland and Tom Read Cutting.

 Music © 2015 Alex Day
