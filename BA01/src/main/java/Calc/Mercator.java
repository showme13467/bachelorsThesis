package Calc;

//code from https://github.com/eugenp/tutorials/tree/master/java-math/src/main/java/com/baeldung/algorithms/mercator
//class for converting geo coords into int values
abstract class Mercator {
    final static double RADIUS_MAJOR = 6378137.0;
    final static double RADIUS_MINOR = 6356752.3142;

    abstract double yAxisProjection(double input);
    abstract double xAxisProjection(double input);


    static class EllipticalMercator extends Mercator {
        @Override
        double yAxisProjection(double input) {

            input = Math.min(Math.max(input, -89.5), 89.5);
            double earthDimensionalRateNormalized = 1.0 - Math.pow(Mercator.RADIUS_MINOR / Mercator.RADIUS_MAJOR, 2);

            double inputOnEarthProj = Math.sqrt(earthDimensionalRateNormalized) *
                    Math.sin( Math.toRadians(input));

            inputOnEarthProj = Math.pow(((1.0 - inputOnEarthProj) / (1.0+inputOnEarthProj)),
                    0.5 * Math.sqrt(earthDimensionalRateNormalized));

            double inputOnEarthProjNormalized =
                    Math.tan(0.5 * ((Math.PI * 0.5) - Math.toRadians(input))) / inputOnEarthProj;

            return (-1) * Mercator.RADIUS_MAJOR * Math.log(inputOnEarthProjNormalized);
        }

        @Override
        double xAxisProjection(double input) {
            return Mercator.RADIUS_MAJOR * Math.toRadians(input);
        }
    }



    static class SphericalMercator extends Mercator {

        @Override
        double xAxisProjection(double input) {
            return Math.toRadians(input) * RADIUS_MAJOR;
        }

        @Override
        double yAxisProjection(double input) {
            return Math.log(Math.tan(Math.PI / 4 + Math.toRadians(input) / 2)) * RADIUS_MAJOR;
        }
    }

}