package online.sanen.cdm.factory;

import com.mhdt.annotation.Priority;

import online.sanen.cdm.Behavior;


@Priority
public class Topic implements Behavior<Topic>{
	
	int id;
	
	String name;
	
	
	
	public Topic() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Topic(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String primarykey() {
		return "id";
	}

	@Override
	public String toString() {
		return "Topic [id=" + id + ", name=" + name + "]";
	}
	
}
