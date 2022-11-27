package com.cats.ems.dto;

import java.util.List;
import java.util.Objects;
import lombok.Data;

@Data
public class LeaveAllEmployeeRoot {
		private long employeeId;
	    private String name;
	    private List<LeaveAllEmployee> leaveAllEmployee;
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			LeaveAllEmployeeRoot other = (LeaveAllEmployeeRoot) obj;
			return employeeId == other.employeeId && Objects.equals(name, other.name);
		}
		@Override
		public int hashCode() {
			return Objects.hash(employeeId, name);
		}
	    
	    

}
