/**
 * 
 */
package com.unittest.unitest.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.unittest.unitest.model.ExcelEntities;

/**
 * @author Supriya.Srivastava
 *
 */
@Repository
public interface ExcelEntitiesDao extends JpaRepository<ExcelEntities, Integer>{
	
	
	
	
	
}

