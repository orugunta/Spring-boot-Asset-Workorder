package com.hari.spb.react.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hari.spb.react.entitybean.WorkOrderDetails;

@Repository
public interface WorkOrderDao extends JpaRepository<WorkOrderDetails, Long> {

	@Query("SELECT wod FROM WorkOrderDetails wod where wod.assetMapping.assetId = :assetId")
	List<WorkOrderDetails> findAllByAssetId(@Param("assetId") String assetId);
}
