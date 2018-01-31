[![Build Status](https://travis-ci.org/michaelrice/WeakSSL.svg)](https://travis-ci.org/michaelrice/WeakSSL)
# grails-weak-ssl

This plugin will deliberately break the Java SSL security model.
It is useful for working in development environments or test
environments where you have a generated SSL certificate. Notice
one of the modules detects the Grails test mode and reads the
Grails generated SSL cert and provides an SSL exception for it.


# Issues
Submit issues to the [GitHub Issue Tracker](https://github.com/michaelrice/WeakSSL/issues)


## Authors
* Shawn Hartsock -- Creator
* Michael Rice -- Maintainer for grails 2.X
* Sachin Verma -- Maintainer for grails 3.x
