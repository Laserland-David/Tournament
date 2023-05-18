/**
 * 
 */
/**
 * @author Server
 *
 */
module SoloTournament {
	requires org.apache.poi.poi;
	requires gson;
	requires java.sql;
	opens tournament;
	exports tournament;
}