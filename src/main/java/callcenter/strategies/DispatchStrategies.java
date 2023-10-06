package callcenter.strategies;

import callcenter.Call;
import callcenter.Employee;

import java.util.ArrayList;
import java.util.Optional;

public class DispatchStrategies {

    public static Optional<Employee> apply(final Call call, final ArrayList<Employee> employees) {
        return employees.stream()
                .filter(Employee::isFree)
                .findFirst();
    }

}
