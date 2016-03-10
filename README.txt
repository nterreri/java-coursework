This archive contains the deliverables for 
GC01 MSc CS UCL 2015 
 Niccolo Terreri

1. Workspace and source code
2. Runnable jar file
3. Javadoc folder
4. A list of all features implemented
5. A video demonstration (which was surprisingly difficult to produce)
6. The present README

The JAR file has been tested to work both linux and Windows 7.
The JAR file should require no external dependencies, and the external
libraries it relies on (both of which are open source) are packaged inside the JAR.
The JAR file requires a "patient_records" and "login_data" file to work,
but it should be able to self-extract them should they be missing
(they are added as resources to the project buildpath, and can be found
inside the archive).

The Javadoc references the used external libraries, as well as several
stackoverflow.com posts.

The video demonstration illustrates all the features that were listed as
required and optional for Option 1 in the coursework specification.

Working on this coursework has been quite draining. The program itself clearly
requires some polishing (there is no security for the login, the UI is just
the default "Metal" look&feel, etc.), but the main features should all be in
place.

Github repository, will become public on 08/12/15:
https://github.com/nterreri/java-coursework

NT
