
# polyvdw

### A computational exploration of Polynomial van der Waerden numbers

### Results
* Proved there exists an elementary configuration bounding PW(4, {x^2}) < 1+290085289^2
* Proved there exists an elementary configuration bounding PW(4, {x^2+x}) < 1+113261*113262
* Proved there exist elementary configurations bounding PW(4, {ax^2+bx}) for all a=1, b <= 2000, and some with a > 1 [see here](https://cryptopocalyp.se/assets/bounds.pdf)
* Proved that there exist elementary configurations bounding PW(3, {ax^2+bx})
* Found a "Pythagorean K4" for {x^2+x}

### Instructions
First, adjust output path `OUT_PATH` in `PolyRunner` and `ThreeColorRunner`.
To run, compile from the `src/java` directory with `javac polyvdw/*.java`.
Then run the 4-color version with `java polyvdw/PolyRunner` or the 3-color version with `java polyvdw/ThreeColorRunner`.
