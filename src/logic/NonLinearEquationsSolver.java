package logic;

import exceptions.NoRootException;

public class NonLinearEquationsSolver {

    public static Answer solveWithMiddleDivision(CustomSimpleFunction func, double leftBorder, double rightBorder, double acc) throws NoRootException {
        checkIfSingleRootExist(func, leftBorder, rightBorder);

        int i = 1;
        double curX = (leftBorder + rightBorder) / 2;

        while(Math.abs(rightBorder - leftBorder) > acc && Math.abs(func.getVal(curX)) > acc){
            i++;
            //Выбираем границу в следующей итерации (знаки функции должны быть различны на концах)
            if(Math.signum(func.getVal(curX)) * Math.signum(func.getVal(leftBorder)) < 0) rightBorder = curX;
            else leftBorder = curX;

            curX = (leftBorder + rightBorder) / 2;
        }
        return new Answer(i, curX, func.getVal(curX));
    }

    public static Answer solveWithNewtonMethod(CustomSimpleFunction func, double leftBorder, double rightBorder, double acc) throws NoRootException {
        checkIfSingleRootExist(func, leftBorder, rightBorder);

        CustomSimpleFunction firstDer = func.derivative(1);
        CustomSimpleFunction secondDer = firstDer.derivative(1);
        int i = 0;
        double prevX, curX, div;
        // Если произведение функции и второй производной больше нуля (значения имеют одинаковые знаки),
        // то метод обеспечит быструю сходимость
        if(func.getVal(leftBorder) * secondDer.getVal(leftBorder) > 0)
            curX = leftBorder;
        else curX = rightBorder;

        //отношение функции к первой производной в предыдущей точке.
        //Чтобы не высчитывать заново в условии для выхода из цикла
        div = func.getVal(curX) / firstDer.getVal(curX);

        do{
            i++;
            prevX = curX;
            curX = prevX - div;
            div = func.getVal(curX) / firstDer.getVal(curX);

        }while(Math.abs(curX - prevX) > acc
                || Math.abs(func.getVal(curX)) > acc
                || Math.abs(div) > acc);

        return new Answer(i, curX, func.getVal(curX));
    }

    public static Answer solveWithIterationMethod(CustomSimpleFunction func, double leftBorder, double rightBorder, double acc) throws NoRootException {
        checkIfSingleRootExist(func, leftBorder, rightBorder);

        double curX, prevX;
        int i = 0;
        double lambda;
        CustomSimpleFunction firstDer = func.derivative(1);

        //В знаменатель в лямбде должно идти наибольшее значение производной
        if(firstDer.getVal(leftBorder) > firstDer.getVal(rightBorder)) {
            curX = leftBorder;
        }else{
            curX = rightBorder;
        }

        lambda = -1 / firstDer.getVal(curX);
        curX = curX + lambda * func.getVal(curX);

        do{
            i++;
            prevX = curX;
            curX = prevX + lambda * func.getVal(prevX);


        }while(Math.abs(curX - prevX) > acc);
        return new Answer(i, curX, func.getVal(curX));
    }


    public static boolean checkIfSingleRootExist(CustomSimpleFunction func, double leftBorder, double rightBorder) throws NoRootException {
        //Для проверки единственности корня
        if(Math.signum(func.getVal(leftBorder))*Math.signum(func.getVal(rightBorder)) > 0)
            throw new NoRootException("Невозможно найти единственный корень на данном интервале");

        CustomSimpleFunction firstDer = func.derivative(1);
        double sign = Math.signum(firstDer.getVal(leftBorder));
        double step = (rightBorder - leftBorder) / 1000;
        for(; leftBorder < rightBorder; leftBorder += step ){
            if(Math.signum(firstDer.getVal(leftBorder)) != sign)
                throw new NoRootException("Невозможно найти единственный корень на данном интервале");
        }
        return true;
    }
}
