package test.java.com.cm.server.utils;

import java.io.IOException;
import java.io.ObjectOutputStream;

import main.java.com.cm.server.utils.Database;
import junit.framework.TestCase;

public class DatabaseTestCase extends TestCase {
	
	public void testUserRegistration() {
		Database memory = new Database();
		
		try {
			
			// prepare User1
			String user1 = "User1";
			ObjectOutputStream connection1 = new ObjectOutputStream(System.out);
			// prepare User2
			String user2 = "User2";
			ObjectOutputStream connection2 = new ObjectOutputStream(System.out);

			// Inject user 1 into storage memory
			memory.InsertUser(user1, connection1);
			
			String user = memory.GetUserID(connection1);
			assertTrue("it should return the same user", user.equals(user1));
			user = memory.GetUserID(connection2);
			assertFalse("it should not be equal to user2", user2.equals(user));
			
		} catch (IOException e) {
			fail();
		}

	}
	

}
