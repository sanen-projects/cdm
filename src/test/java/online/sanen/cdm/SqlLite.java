package online.sanen.cdm;

import java.util.List;

import com.mhdt.annotation.BootStrapID;
import com.mhdt.annotation.NoInsert;
import com.mhdt.annotation.Table;

import online.sanen.cdm.basic.BasicBean;
import online.sanen.cdm.basic.Driven;
import online.sanen.cdm.basic.Sorts;
import online.sanen.cdm.condition.C;
import online.sanen.cdm.condition.Condition;
import online.sanen.cdm.factory.BootstrapFactoty;

public class SqlLite {
	
	@Table("user") // Set the table name to the class name by default
	@BootStrapID("defaultBootstrap")	// Identifies the bootstrap id
	public static class User implements Behavior<User>{
		
		@NoInsert
		int id;
		String name;
		
		public User() {
			
		}

		public User(int id) {
			this.id = id;
		}

		@Override
		public String toString() {
			return "User [id=" + id + ", name=" + name + "]";
		}

		@Override
		public String primarykey() {
			return "id";
		}
	}

	public static void main(String[] args) {

		Bootstrap bootstrap = BootstrapFactoty.load("defaultBootstrap",obstract -> {
			obstract.setDriver(Driven.SQLITE);
			obstract.setUrl("jdbc:sqlite:test.sqlite");
		});
		
	}

}
