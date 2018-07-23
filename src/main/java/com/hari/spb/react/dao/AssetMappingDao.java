package com.hari.spb.react.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hari.spb.react.entitybean.AssetMapping;

@Repository
public interface AssetMappingDao extends JpaRepository<AssetMapping, String> {

	@Query("SELECT am FROM AssetMapping am where am.kksId = :kksId")
	AssetMapping findByKksId(@Param("kksId") String kksId);

	@Query("SELECT am FROM AssetMapping am where am.assetId = :assetId")
	List<AssetMapping> findAllByAssetId(@Param("assetId") String assetId);

	@Query("SELECT am FROM AssetMapping am where am.assetId = :assetId AND am.kksId = :kksId")
	AssetMapping findByAssetIdAndKksId(@Param("kksId") String kksId, @Param("assetId") String assetId);
}
