package edu.uoregon.scrumthing;

import java.util.Iterator;

// A doubly linked list implementation that allows a node to remove itself in O(1) time.
public class ControllerPool implements Iterable<Controller> {
	private ControllerNode head;
	private ControllerNode tail;
	
	public class ControllerNode {
		private Controller controller;
		private ControllerNode prev;
		private ControllerNode next;
		private ControllerPool pool;
		
		public ControllerNode(Controller controller, ControllerNode prev, ControllerNode next, ControllerPool pool) {
			this.controller = controller;	
			this.prev = prev;
			this.next = next;
			this.pool = pool;
			
			// Allow controller to remove itself from the pool
			controller.registerNode(this);
		}
		
		public void removeSelf() {
			if (this.prev == null) pool.head = this.next;
			else this.prev.next = this.next;
			if (this.next == null) pool.tail = this.prev;
			else this.next.prev = this.prev;
		}
		
		public Controller getController() {
			return this.controller;
		}
	}
	
	public ControllerPool() {
		head = null;
		tail = null;
	}
	
	public void add(Controller controller) {
		if (tail == null) {
			head = new ControllerNode(controller, null, null, this);
			tail = head;
		} else {
			this.tail.next = new ControllerNode(controller, tail, null, this);
			this.tail = this.tail.next;
		}
	}
	
	public void closeAll() {
		Iterator<Controller> iter = iterator();
		while (iter.hasNext()) {
			Controller controller = iter.next();
			if (!controller.closeAddressBook()) break;
		}
	}
	
	@Override
	public Iterator<Controller> iterator() {
		return new Iterator<Controller>() {
			private ControllerNode cur;
			@Override
			public boolean hasNext() {
				if (cur == null) {
					cur = head;
				}
				return cur != null;
			}

			@Override
			public Controller next() {
				if (cur == null) {
					cur = head;
				}
				if (cur == null) return null;
				Controller res = cur.getController();
				cur = cur.next;
				return res;
			}
		};
	}
}
