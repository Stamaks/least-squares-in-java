Least squares in Java
=====================

Java Least Squares fitting library.
-----------------------------------

This is a small least squares fitting library made in java.
It was originally used in the development of an image analysis tool 
SpeckleTrackerJ. http://athena.physics.lehigh.edu/speckletrackerj/ I took 
it from odinsbane (https://github.com/odinsbane) and now it has a GUI. English
version is coming soon!

The outline of using this library is you have a set of data that you
would like fit a function to. Define the Function, how it evaluates
the input and paramters. Create a Fitter (three types currently) initialized
to the function. Set the fitter with the data and make an initial guess of the
parameters. Tell the fitter to find the best set of parameters which minimizes the
sum of squares of error.

Examples of console using can be found on odinsbane account.
Here is an example of using it in GUI
-------

Lets say we have some data.

x : y
0 : 1.0
1 : 0.9
2 : 1.0
3 : 1.3
4 : 1.8
5 : 2.5

And we want to look on it's graph. THan we should write this way on a field below:

0, 1.0; 1, 0.9; 2, 1.0; 3, 1.3; 4, 1.8; 5, 2.5

Change or do not change the order of the optimised function below it in a small window.
Then press the button and enjoy :)


Fitter implementations
----------------------

LinearFitter: for use with equations that are linearly dependent on the input parameters. Standard
linear regression where derivatives are taken by setting all of the parameters to zero, except for the
 one of interest.

 NonLinearSolver: Similar to the LinearSolver, except it will run multiple iterations, there is a damping
  factor, and the derivatives are calculated by taken by varying the parameters a small amount from the
  previous guess.

 MarquardtFitter: Similar to the NonLinearSolver except the damping is adaptive.
