package it.gov.pagopa.swclient.mil.feecalculator.dao;

import org.bson.codecs.pojo.annotations.BsonId;

import io.quarkus.mongodb.panache.common.MongoEntity;

/**
 * Entity bean mapping the configuration of a PSP when connecting to the node
 */
@MongoEntity(database = "mil", collection = "pspconf")
public class PspConfEntity {

	/*
	 * 
	 */
	@BsonId
	public String acquirerId;

	/*
	 * 
	 */
	public PspConfiguration pspConfiguration;
}
