package callcenter;

import callcenter.strategies.DispatchStrategies;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Queue;

public class CallCenter {

    private final ArrayList<Employee> operators = new ArrayList();
    private final ArrayList<Employee> supervisors = new ArrayList();
    private final ArrayList<Employee> directors = new ArrayList();

    private final Deque<Call> callQueue = new LinkedList();

    public CallCenter(
            final ArrayList operators,
            final ArrayList supervisors,
            final ArrayList directors) {
        this.operators.addAll(operators);
        this.supervisors.addAll(supervisors);
        this.directors.addAll(directors);
        new Thread(() -> this.dispatchCall()).start();
    }

    public void dispatchCall() {
        while (!callQueue.isEmpty()) {
            final Call call = this.callQueue.peek();
            ArrayList<Employee> employees = this.operators;
            switch (call.getRank()) {
                case SUPERVISOR -> employees = this.supervisors;
                case DIRECTOR -> employees = this.directors;
            }

            final var employee = DispatchStrategies.apply(call, employees);
            employee.ifPresent(emp -> emp.takeCall(this.callQueue.poll()));
        }
    }
}
