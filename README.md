# PSM planner

Authors:
* Jan Tozicka (jan.tozicka@agents.fel.cvut.cz) - planner
* Jan Jakubuv (jan.jakubuv@agents.fel.cvut.cz) - planner
* Martin Svatos (svatoma1@fel.cvut.cz) - reductions
* Emirhan Ozsoy (emirhan.ozsoy@gmail.com) - SecreC set intersection
* Michal Stolba (michal.stolba@agents.fel.cvut.cz) - privacy extension
* Antonin Komenda (antonin.komenda@fel.cvut.cz) - principle

Planner is described in the following articles:
* Planner description without -D (internal dependencies reduction)
  * Privacy-concerned multiagent planning [here](http://link.springer.com/article/10.1007%2Fs10115-015-0887-7)
* Decription of internal dependencies reduction
   Recursive Reductions of Internal Dependencies in Multiagent Planning (Proceedings of ICAART 2016)

## Building & running:
``` bash
# build Fast Downward (http://www.fast-downward.org/)
cd downward-src
./build_all

# Set up maven (see bellow)

# install polystar
cd ..
sudo cp polystar-bin/* /usr/local/bin/

# build PSM planner
cd FSM
mvn package -DskipTests

# run example
chmod +x testPSM-CoDMAP.sh
./testPSM-CoDMAP.sh
```

## Set up Maven

Note that the maven needs to be set up to connect to ATG repositories to can get dependencies for project:
The file "settings.xml" is placed in ".m2" directory.
* For Windows - C:\Users\USER_NAME\.m2
* For Linux - /home/USER_NAME/.m2

If directory .m2 does not exist at you home directory, then you have to create new directory with name .m2

You need to download file settings.xml from https://github.com/gree7/psm-planner/blob/master/settings.xml.
* If file does not exist, then you can put there this file settings.xml.
* If file exists, but is empty - <setting> element does not include some sub-element, then replace settings.xml file by this file settings.xml.
* If file exist and <setting> element contains some sub-elements, then copy content of <setting> element from this file settings.xml.
