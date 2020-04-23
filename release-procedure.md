# Steps to perform a release

## First release

You need a Nexus OSS account and a GPG key in order to generate required `.asc` files.

In ~/.gradle/gradle.properties, define the following properties:

NEXUS_USERNAME=(username)  
NEXUS_PASSWORD=(password)  
signing.keyId=(gpg1 key id, or last 8 chars of gpg2 key)  
signing.password=gpg password  
signing.secretKeyRingFile=/Users/username/.gnupg/secring.gpg  

Correct the properties. Create a gpg key if you have not yet one.

## Every release

1. assemble local build with correct version as last final check:
    cd to gdx-jnigen and run: "./gradlew -P RELEASE clean build"

2. upload archives via console:
    cd to gdx-jnigen and run: "./gradlew -P RELEASE publish"

3. for RELEASE go to https://oss.sonatype.org --> "Staging Repositories"
   - select "Close", then "Release" to fully release (needs signing!)
   - signing: needs that sonatype key from above...

4. update e.g. gdx-jnigen README.md --> search & replace version numbers!
   update/tag git: e.g. v0.7.0
   update version to e.g. 0.8.0 in gradle.properties
