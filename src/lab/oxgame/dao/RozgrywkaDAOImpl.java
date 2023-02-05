package lab.oxgame.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lab.oxgame.datasource.DataSource;
import lab.oxgame.model.OXEnum;
import lab.oxgame.model.Rozgrywka;

public class RozgrywkaDAOImpl implements RozgrywkaDAO{

	private static final Logger logger = 
			LoggerFactory.getLogger(RozgrywkaDAOImpl.class);
	
	@Override
	public int zapiszRozgrywke(Rozgrywka rozgrywka) {
		int liczbaDodanychWierszy = 0;
		
		String query = "INSERT INTO rozgrywka(gracz_o, gracz_x, zwyciezca, dataczas_rozgrywki) VALUES (?, ?, ?, ?)";
		try (Connection connect = DataSource.getConnection(); PreparedStatement preparedStmt = connect.prepareStatement(query, Statement.RETURN_GENERATED_KEYS))
		{
			preparedStmt.setString(1, rozgrywka.getGraczO());
			preparedStmt.setString(2, rozgrywka.getGraczX());
			preparedStmt.setString(3, rozgrywka.getZwyciezca().toString());
			preparedStmt.setObject(4, rozgrywka.getDataczasRozgrywki());
			liczbaDodanychWierszy = preparedStmt.executeUpdate();
			if (liczbaDodanychWierszy > 0)
			{
				ResultSet keys = preparedStmt.getGeneratedKeys();
				if(keys.next())
				{
					rozgrywka.setRozgrywkaId(keys.getInt(1));
				}
				keys.close();
			}
		}
		catch (SQLException e)
		{
			logger.error("B³¹d podczas zapisywania rozgrywki!", e);
		}
		
		return liczbaDodanychWierszy;
	}

	@Override
	public List<Rozgrywka> pobierzRozgrywki(Integer odWiersza, Integer liczbaWierszy) {
		List<Rozgrywka> rozgrywki = new ArrayList<>();
		
		String query = "SELECT * FROM rozgrywka ORDER BY dataczas_rozgrywki DESC" + (odWiersza != null ? " OFFSET ?" : "") + (liczbaWierszy != null ? " LIMIT ?" : "");
		try (Connection connect = DataSource.getConnection(); PreparedStatement preparedStmt = connect.prepareStatement(query))
		{
			
			if (odWiersza != null)
			{
				preparedStmt.setInt(1, odWiersza);
			}
			if (liczbaWierszy != null)
			{
				preparedStmt.setInt(odWiersza != null ? 2 : 1, liczbaWierszy);
			}
			
			ResultSet rs = preparedStmt.executeQuery();
			while(rs.next())
			{
				Integer rozgrywkaId = rs.getInt("rozgrywka_id");
				String graczO = rs.getString("gracz_o");
				String graczX = rs.getString("gracz_x");
				OXEnum zwyciezca = OXEnum.fromString(rs.getString("zwyciezca"));
				LocalDateTime dataczasRozgrywki = rs.getTimestamp("dataczas_rozgrywki").toLocalDateTime();
				Rozgrywka rozgrywka = new Rozgrywka(rozgrywkaId, graczX, graczO, zwyciezca, dataczasRozgrywki);
				rozgrywki.add(rozgrywka);
			}
			rs.close();
			
		}
		catch (SQLException e)
		{
			logger.error("B³¹d podczas pobierania rozgrywki!", e);
		}
		
		return rozgrywki;
	}

	@Override
	public int usunRozgrywki() {
		int liczbaUsunietychWierszy = 0;
		
		String query = "DELETE FROM rozgrywka";
		try (Connection connect = DataSource.getConnection(); PreparedStatement preparedStmt = connect.prepareStatement(query))
		{
			liczbaUsunietychWierszy = preparedStmt.executeUpdate();
		}
		catch (SQLException e)
		{
			logger.error("B³¹d podczas usuwania rozgrywki!", e);
		}
		
		return liczbaUsunietychWierszy;
	}
	
}
