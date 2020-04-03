package logic;

import exceptions.NoRootException;

import java.beans.SimpleBeanInfo;

public class NonLinearEquationsSolver {

    public static Answer solveWithMiddleDivision(CustomSimpleFunction func, double leftBorder, double rightBorder, double acc) throws NoRootException {
        if(!isRootExists(func, leftBorder, rightBorder)) throw new NoRootException("Корня на данном интервале не существует");
        int i = 1;
        double curX = (leftBorder + rightBorder) / 2;
        while(Math.abs(rightBorder - leftBorder) > acc && Math.abs(func.getVal(curX)) > acc){
            i++;
            if(Math.signum(func.getVal(curX)) * Math.signum(func.getVal(leftBorder)) < 0) rightBorder = curX;
            else leftBorder = curX;

            curX = (leftBorder + rightBorder) / 2;
        }
        return new Answer(i, curX, func.getVal(curX));
    }

    public static Answer solveWithNewtonMethod(CustomSimpleFunction func, double leftBorder, double rightBorder, double acc) throws NoRootException {
        if(!isRootExists(func, leftBorder, rightBorder)) throw new NoRootException("Корня на данном интервале не существует");


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
        if(!isRootExists(func, leftBorder, rightBorder)) throw new NoRootException("Корня на данном интервале не существует");

        double curX, prevX;
        int i = 0;
        double lambda;
        CustomSimpleFunction firstDer = func.derivative(1);
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


    public static boolean isRootExists(CustomSimpleFunction func, double leftBorder, double rightBorder){
        return Math.signum(func.getVal(leftBorder))*Math.signum(func.getVal(rightBorder)) <= 0;
    }
}
