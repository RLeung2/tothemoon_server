package com.restapi.ToTheMoon;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.util.Pair;
import org.jgrapht.*;
import org.jgrapht.generate.*;
import org.jgrapht.graph.*;
import org.jgrapht.traverse.*;
import org.jgrapht.util.*;
import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.MatchingAlgorithm.Matching;
import org.jgrapht.alg.matching.MaximumWeightBipartiteMatching;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.WeightedMultigraph;
import org.locationtech.jts.geom.Geometry;

@SuppressWarnings({ "unused" })
public class Districting {

	private int id;
	private ObjectiveFunction objectivefunction;
	private List<District> districts;
	
	public Districting() {
		
	}
	
	public Districting(int id, ObjectiveFunction objectivefunction, List<District> districts) {
		super();
		this.id = id;
		this.objectivefunction = objectivefunction;
		this.districts = districts;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ObjectiveFunction getObjectivefunction() {
		return objectivefunction;
	}

	public void setObjectivefunction(ObjectiveFunction objectivefunction) {
		this.objectivefunction = objectivefunction;
	}

	public List<District> getDistricts() {
		return districts;
	}

	public void setDistricts(List<District> districts) {
		this.districts = districts;
	}
	
	/*All the methods below will be methods that do actual calculations 
	 * */
	public float calculateDevFromAvg() {
		return (float) 0.0;
	}
	
	public float calculateSplitCountyScore() {
		return (float) 0.0;
	}
	
	public float calculateObjScore() {
		return (float) 0.0;
	}
	
	public float calculateEnactedAreaScore() {
		return (float) 0.0;
	}
	
	public float calculateEnactedPopScore() {
		return (float) 0.0;
	}
	
	public float calculateMajMinScore() {
		return (float) 0.0;
	}
	
	public float calculateAreaPairDevScore() {
		return (float) 0.0;
	}
	
	public float calculatePopEqualityScore() {
		return (float) 0.0;
	}
	
	public float calculateCompactness(Constraints compactnessType) {
		return (float) 0.0;
	}
	
	private static List<Float> calculatePolsbyPopperScore(List<Geometry> districtGeometryList) {
		List<Float> polsbyPopperScores = new ArrayList<Float>();
		
		for (int i = 0; i < districtGeometryList.size(); i++) {
			Geometry currDistrictGeometry = districtGeometryList.get(i);
			double currDistrictArea = currDistrictGeometry.getArea();
			double currDistrictPerimeter = currDistrictGeometry.getLength();
			
			float ppScore = (float) ((4 * Math.PI * currDistrictArea) / (Math.pow(currDistrictPerimeter, 2)));
			polsbyPopperScores.add(ppScore);
		}
		return polsbyPopperScores;
	}
	
	public float calculateEfficiencyGap() {
		return (float) 0.0;
	}
	
	public void gillConstructRenumbering(Districting enactedDistricting) {
		List<District> enactedDistrictsList = enactedDistricting.getDistricts();
		Graph<District, DefaultWeightedEdge> bipartiteGraph = generateBipartiteGraph(enactedDistrictsList);
		Set<District> enactedDistrictsSet = new HashSet<District>(enactedDistrictsList);
		Set<District> currentDistrictsSet = new HashSet<District>(this.districts);
		MaximumWeightBipartiteMatching<District, DefaultWeightedEdge> bipartiteMatcher = 
				new MaximumWeightBipartiteMatching<District, DefaultWeightedEdge>(bipartiteGraph, enactedDistrictsSet, currentDistrictsSet);
		Matching<District, DefaultWeightedEdge> bestMatching = bipartiteMatcher.getMatching();
		HashMap<District, District> reorderedMapping = generateReorderedMapping(bestMatching);
		reorderDistricts(reorderedMapping, enactedDistrictsList);
	}
	
	private Graph<District, DefaultWeightedEdge> generateBipartiteGraph(List<District> enactedDistrictsList) {
		HashMap<Pair<District, District>, Integer> intersectionMap = generateIntersectionMap(enactedDistrictsList);
		Graph<District, DefaultWeightedEdge> bipartiteGraph = new WeightedMultigraph<District, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		for(int i = 0; i < enactedDistrictsList.size(); i++) {
			bipartiteGraph.addVertex(enactedDistrictsList.get(i));
			bipartiteGraph.addVertex(this.districts.get(i));
		}
		for (Map.Entry<Pair<District, District>, Integer> mapEntry : intersectionMap.entrySet()) {
			Pair<District, District> districtPair = mapEntry.getKey();
			int intersectionPopulation = mapEntry.getValue();
			double intersectionEdgeWeight = intersectionPopulation != 0 ? 1 / (double)intersectionPopulation : 0;
			DefaultWeightedEdge edge = bipartiteGraph.addEdge(districtPair.getKey(), districtPair.getValue());
			bipartiteGraph.setEdgeWeight(edge, intersectionEdgeWeight);
		}
		return bipartiteGraph;
	}
	
	private HashMap<Pair<District, District>, Integer> generateIntersectionMap(List<District> enactedDistrictsList) {
		HashMap<Pair<District, District>, Integer> intersectionMap = new HashMap<Pair<District, District>, Integer>();
		
		for (int i = 0; i < enactedDistrictsList.size(); i++) {
			District enactedDistrict = enactedDistrictsList.get(i);
			Collection<Precinct> enactedPrecincts = enactedDistrict.getPrecincts();

			for (int j = 0; j < this.districts.size(); j++) {
				District comparedDistrict = this.districts.get(j);
				Collection<Precinct> comparedPrecincts = comparedDistrict.getPrecincts();
				Collection<Precinct> commonPrecincts = this.getCommonPrecincts(enactedPrecincts, comparedPrecincts);	
				int intersectionPopulation = 0;
				for (Precinct precinct: commonPrecincts) {
					intersectionPopulation += precinct.getPopulation();
				}
				Pair<District, District> districtPair = new Pair<District, District>(enactedDistrict, comparedDistrict);
				intersectionMap.put(districtPair, intersectionPopulation);
			}
		}
		return intersectionMap;
	}
	
	private Collection<Precinct> getCommonPrecincts(Collection<Precinct> enactedPrecincts, Collection<Precinct> comparedPrecincts) {
		Collection<Precinct> commonPrecincts = new ArrayList<Precinct>();
		for (Precinct enactedPrecinct: enactedPrecincts) {
			for (Precinct comparedPrecinct: comparedPrecincts) {
				if (enactedPrecinct.getId() == comparedPrecinct.getId())
						commonPrecincts.add(enactedPrecinct);
			}
		}
		return commonPrecincts;
	}
	
	private HashMap<District, District> generateReorderedMapping(Matching<District, DefaultWeightedEdge> matching) {
		Graph<District, DefaultWeightedEdge> bipartiteGraph = matching.getGraph();
		HashMap<District, District> reorderedMapping = new HashMap<District, District>();
		Set<DefaultWeightedEdge> bestEdges = matching.getEdges();
		Iterator<DefaultWeightedEdge> set = bestEdges.iterator();
		while(set.hasNext()){
			DefaultWeightedEdge edge = set.next();
			District enactedSource = bipartiteGraph.getEdgeSource(edge);
			District currentTarget = bipartiteGraph.getEdgeTarget(edge);
			reorderedMapping.put(enactedSource, currentTarget);
	     }
		return reorderedMapping;
	}
	
	private void reorderDistricts(HashMap<District, District> reorderedMapping, List<District> enactedDistrictsList) {
		List<District> reorderedDistrictsList= new ArrayList<District>();
		for (int i = 0; i < enactedDistrictsList.size(); i++) {
			District currentDistrict = reorderedMapping.get(enactedDistrictsList.get(i));
			reorderedDistrictsList.add(currentDistrict);
		}
		this.setDistricts(reorderedDistrictsList);
	}
	
	public void sortDistrictsByMinority(MinorityPopulation minority) {
		Collections.sort(this.districts, getMinorityPercentageComparator(minority));
	}
	
	Comparator<District> getMinorityPercentageComparator(final MinorityPopulation minority) {
	    return new Comparator<District>() {
		    @Override
		    public int compare(District d1, District d2) {
		    	Float d1Percentage = d1.getMinorityPopulationPercentages().get(minority);
		    	Float d2Percentage = d2.getMinorityPopulationPercentages().get(minority);
		        return d1Percentage.compareTo(d2Percentage);
		    }
		};
	}
	
}
