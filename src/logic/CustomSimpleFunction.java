package logic;

import edu.hws.jcm.data.Expression;
import edu.hws.jcm.data.Function;
import edu.hws.jcm.data.SimpleFunction;
import edu.hws.jcm.data.Variable;

public class CustomSimpleFunction extends SimpleFunction {
    private Variable varX;
    private Expression exp;

    public CustomSimpleFunction(Expression expression, Variable variable) {
        super(expression, variable);
        this.varX = variable;
        this.exp = expression;
    }

    public double getVal(double var) {
        return super.getVal(new double[]{var});
    }

    @Override
    public CustomSimpleFunction derivative(int var1) {
            return new CustomSimpleFunction(this.exp.derivative(this.varX), this.varX);
    }
}
