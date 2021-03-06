package com.tw;

import com.tw.exception.IncompatibleTicketTypeException;
import com.tw.exception.InvalidTicketException;
import com.tw.exception.LockerIsFullException;
import com.tw.robot.SuperLockerRobot;
import org.junit.Test;
import static org.junit.Assert.*;

public class SuperLockerRobotTest {
    @Test
    public void should_return_ticket_and_save_to_biggest_rate_locker_when_robot_save_bag_given_second_locker_has_biggest_available_capacity_and_large_bag() {
        StorableFactory factory = new StorableFactory();
        SuperLockerRobot robot = (SuperLockerRobot) factory.createStorable(Size.Large, 2);
        for (int i = 0; i < 20; i++) {
            robot.getLockers().get(0).store(new Bag(Size.Large));
        }
        for (int i = 0; i < 10; i++) {
            robot.getLockers().get(1).store(new Bag(Size.Large));
        }

        Bag expectBag = new Bag(Size.Large);
        Ticket ticket = robot.store(expectBag);

        assertNotNull(ticket);
        assertTrue(robot.getLockers().get(1).containBag(expectBag));
    }

    @Test
    public void should_return_ticket_and_save_to_first_locker_when_robot_save_bag_given_all_locker_has_same_rate_and_medium_bag() {
        StorableFactory factory = new StorableFactory();
        SuperLockerRobot robot = (SuperLockerRobot) factory.createStorable(Size.Large, 2);

        for (int i = 0; i < 10; i++) {
            robot.getLockers().get(0).store(new Bag(Size.Large));
            robot.getLockers().get(1).store(new Bag(Size.Large));
        }

        Bag expectBag = new Bag(Size.Large);
        Ticket ticket = robot.store(expectBag);

        assertNotNull(ticket);
        assertTrue(robot.getLockers().get(0).containBag(expectBag));
        assertFalse(robot.getLockers().get(1).containBag(expectBag));
    }

    @Test(expected = LockerIsFullException.class)
    public void should_throw_full_when_robot_save_bag_given_lockers_are_full() {
        StorableFactory factory = new StorableFactory();
        SuperLockerRobot robot = (SuperLockerRobot) factory.createStorable(Size.Large, 2);

        for (int i = 0; i < Size.Large.capacity * 2; i++) {
            robot.store(new Bag(Size.Large));
        }

        Bag expectBag = new Bag(Size.Large);
        robot.store(expectBag);
    }

    @Test
    public void should_return_bag_when_robot_pickup_bag_given_valid_ticket() {
        StorableFactory factory = new StorableFactory();
        SuperLockerRobot robot = (SuperLockerRobot) factory.createStorable(Size.Large, 2);

        Bag expectBag = new Bag(Size.Large);
        Ticket ticket = robot.store(expectBag);

        assertEquals(expectBag, robot.pickup(ticket));
    }

    @Test(expected = IncompatibleTicketTypeException.class)
    public void should_throw_incompatible_when_robot_pickup_bag_given_ticket_size_incompatible_not_medium() {
        StorableFactory factory = new StorableFactory();
        SuperLockerRobot robot = (SuperLockerRobot) factory.createStorable(Size.Large, 2);

        Ticket ticket = new Ticket(Size.SMALL);

        robot.pickup(ticket);
    }

    @Test(expected = InvalidTicketException.class)
    public void should_throw_invalid_ticket_when_robot_pickup_bag_given_invalid_ticket() {
        StorableFactory factory = new StorableFactory();
        SuperLockerRobot robot = (SuperLockerRobot) factory.createStorable(Size.Large, 2);

        Ticket ticket = new Ticket(Size.Large);

        robot.pickup(ticket);
    }
}
