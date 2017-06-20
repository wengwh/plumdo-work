package com.plumdo.rest.oa;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


public interface OaRepository extends JpaRepository<OaEntity, Long>,JpaSpecificationExecutor<OaEntity> {

}