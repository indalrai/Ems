
package com.cats.ems.repo;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cats.ems.dto.LeaveAndEmployeeData;


@Repository
public class EmployeeRepositoryEntityManager {

	@PersistenceContext
	private EntityManager entityManager; // // public

//	public List<LeaveAndEmployeeData> findAllEmpAndLeave(long id, String dateTime, String dateTime1) {
//		String jpql =  "select em.name, em.employee_id, l1.approved_by, l1.end_date, l1.leave_type_id, l1.start_date, l1.leave_status_id\r\n"
//				+ "from Employee em left join ( select * from leave l where\r\n"
//				+ "        TO_TIMESTAMP(l.start_date, 'dd-MM-yyyy HH24:MI:ss') >=\r\n"
//				+ "             TO_TIMESTAMP( '01-11-2022 00:00:00', 'dd-MM-yyyy HH24:MI:ss')\r\n"
//				+ "        and TO_TIMESTAMP(l.start_date, 'dd-MM-yyyy HH24:MI:ss') <= TO_TIMESTAMP( '30-11-2022 00:00:00', 'dd-MM-yyyy HH24:MI:ss')\r\n"
//				+ "        and (l.leave_status_id = 1 or l.leave_status_id = 2)) l1 on\r\n"
//				+ "    em.employee_id = l1.employee_id where em.manager_id = 2";
//		Query query = entityManager.createNativeQuery(jpql, LeaveAndEmployeeData.class); 
//		return query.getResultList();
//	}
	
//	public List<LeaveAndEmployeeData> findAllEmpAndLeave(long id, String dateTime, String dateTime1) {
//		String jpql = "select em.name as name, em.employee_id as employee_id, l1.approved_by as approved_by, l1.end_date as end_date, l1.leave_type_id as leave_type_id, l1.start_date as start_date, l1.leave_status_id as leave_status_id,em.manager_id as manager_id ,l1.leave_status_id as leave_status_id "
//				+ "from Employee em left join ( select * from leave l where "
//				+ "        TO_TIMESTAMP(l.start_date, 'dd-MM-yyyy HH24:MI:ss') >= "
//				+ "             TO_TIMESTAMP( '"+dateTime+"', 'dd-MM-yyyy HH24:MI:ss') "
//				+ "        and TO_TIMESTAMP(l.start_date, 'dd-MM-yyyy HH24:MI:ss') <= TO_TIMESTAMP( '"+dateTime1+"', 'dd-MM-yyyy HH24:MI:ss') "
//				+ "        and (l.leave_status_id = 1 or l.leave_status_id = 2)) l1 on "
//				+ "    em.employee_id = l1.employee_id where em.manager_id = "+id+"	";
//		Query query = entityManager.createNativeQuery(jpql, LeaveAndEmployeeData.class);
//		System.out.println(query.getResultList());
//		return query.getResultList();
//	}

	public List<LeaveAndEmployeeData> findAllEmpAndLeave(long id, String dateTime, String dateTime1) {
		String jpql = "    select distinct em.name as name, em.employee_id as employee_id, l1.approved_by as approved_by, l1.end_date as end_date, l1.leave_type_id as leave_type_id, l1.start_date as start_date, l1.leave_status_id as leave_status_id,em.manager_id as manager_id ,l1.leave_status_id as leave_status_id ,"
				+ "   case WHEN d.date>=to_date(l1.start_date, 'dd-MM-yyyy')  and d.date<=to_date(l1.end_date,'dd-MM-yyyy') THEN d.date "
				+ "          else null END  as leave_date, l1.leave_status_id, d.date from (select CURRENT_DATE + i as date"
				+ " from generate_series(date '01-11-2022'- CURRENT_DATE, date '30-11-2022' - CURRENT_DATE ) i) as d,Employee em"
				+ " left join (select * from leave l where TO_TIMESTAMP(l.start_date, 'dd-MM-yyyy HH24:MI:ss') >= TO_TIMESTAMP( ?1, 'dd-MM-yyyy HH24:MI:ss')"
				+ "        and TO_TIMESTAMP(l.start_date, 'dd-MM-yyyy HH24:MI:ss') <= TO_TIMESTAMP(?2, 'dd-MM-yyyy HH24:MI:ss')"
				+ "        and (l.leave_status_id = 1 or l.leave_status_id = 2)) l1 on em.employee_id = l1.employee_id where em.manager_id =?3";
		Query query = entityManager.createNativeQuery(jpql, LeaveAndEmployeeData.class);
		query.setParameter(1,dateTime );
		query.setParameter(2, dateTime1);
		query.setParameter(3, id );
		
		
		return query.getResultList();
		
	}
}
