# Java Bindings for PhantomJS-CEF

To run it, you will need a binary distribution of PhantomJS-CEF.

There are several options for you. Please have a look at the PhantomJS-CEF's github page (https://github.com/aknuth/phantomjs-cef)

## Test cases

The test cases are actually just examples.
 
If you decide to use native binaries, you can start the test case using the System Parameter 'phantomjsDir' of the PhantomJS-CEF execution directory.

`e.g. -DphantomjsDir=/home/aknuth/phantomjs-cef`

If you are running the docker way, then should start the test case - the websocket server will be started - then do the docker call:

`e.g. docker run -t -i --rm aknuth/phantomcef ./phantomjava.sh`

this runs the phantomjs-cef binaray and thus the websocket client

You should get a lot of logging output ...


`Have fun` 


