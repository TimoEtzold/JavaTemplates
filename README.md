# JavaTemplates
An Eclipse-Plugin to enable Java Template Code

## This plugin uses third-party libraries
* StringTemplate (http://www.stringtemplate.org) (License: http://www.stringtemplate.org/license.html)
* Apache Commons Lang (http://commons.apache.org/proper/commons-lang/) (License: http://www.apache.org/licenses/LICENSE-2.0)

## Development
### Setting up Eclipse
This plugin was developed using Eclipse SDK 4.6.

If you want to contribute you need "Eclipse SDK", "Eclipse IDE for Eclipse Committers" or "Eclipse IDE for Java Developers".
Make sure the following plugins are installed:

* Git integration for Eclipse (optional)
* m2e - Maven Integration for Eclipse
* Eclipse PDE Plug-in Developer Resources

As this plugin is compiled using maven-tycho, you need to add the 'Tycho Configurator' to Maven.
In Eclipse chose `Window` > `Preferences` > `Maven` > `Discovery` > `Open Catalog` and install `Tycho Configurator`.

### Compile
Right-click on root `pom.xml` and select `Run As` > `Maven build...` and set goals: `clean install`
