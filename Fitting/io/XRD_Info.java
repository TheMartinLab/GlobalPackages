/**
 * @author Eric D. Dill eddill@ncsu.edu
 * @author James D. Martin jdmartin@ncsu.edu
 * Copyright © 2010-2013 North Carolina State University. All rights reserved
 */
package io;

public class XRD_Info {

	enum tags {
		file_name,
		run_name,
		trip,
		single_or_poly,
		run_issues,
		notebook_page_number,
		unwarped_or_raw,
		exists,
		potential_issues,
		popcorn,
		sample_cycle_index,
		sample_number,
		time_per_frame,
		recorded_quench_frame,
		recorded_isotherm_frame,
		recorded_isotherm_time,
		time_quench_start,
		time_isotherm_reached,
		temperature_offset,
		recorded_melt,
		recorded_quench,
		corrected_melt,
		corrected_quench,
		sample_volume,
		sample_size_in_cm,
		beam_size_in_cm,
		recorded_crystallization_frame,
		time_crystallization_estimate_bulk,
		k_est_bulk		
	}
	/* INSTANCE VARIABLES, GETTERS AND SETTERS */
	
	public void setTag(tags tag, String val) {
		switch(tag) {
		case file_name:
			file_name = val;
			return;
		case run_name:
			run_name = val;
			return;
		case trip:
			trip = val;
			return;
		case single_or_poly:
			single_or_poly = val;
			return;
		case run_issues:
			run_issues = val;
			return;
		case notebook_page_number:
			notebook_page_number = val;
			return;
		case unwarped_or_raw:
			unwarped_or_raw = val;
			return;
		case exists:
			exists = val;
			return;
		case potential_issues:
			potential_issues = val;
			return;
		case popcorn:
			popcorn = val;
			return;
		case sample_cycle_index:
			sample_cycle_index = val;
			return;
		case sample_number:
			sample_number = val;
			return;
		case time_per_frame:
			time_per_frame = val;
			return;
		case recorded_quench_frame:
			recorded_quench_frame = val;
			return;
		case recorded_isotherm_frame:
			recorded_isotherm_frame = val;
			return;
		case recorded_isotherm_time:
			recorded_isotherm_time = val;
			return;
		case time_quench_start:
			time_quench_start = val;
			return;
		case time_isotherm_reached:
			time_isotherm_reached = val;
			return;
		case temperature_offset:
			temperature_offset = val;
			return;
		case recorded_melt:
			recorded_melt = val;
			return;
		case recorded_quench:
			recorded_quench = val;
			return;
		case corrected_melt:
			corrected_melt = val;
			return;
		case corrected_quench:
			corrected_quench = val;
			return;
		case sample_volume:
			sample_volume = val;
			return;
		case sample_size_in_cm:
			sample_size_in_cm = val;
			return;
		case beam_size_in_cm:
			beam_size_in_cm = val;
			return;
		case recorded_crystallization_frame:
			recorded_crystallization_frame = val;
			return;
		case time_crystallization_estimate_bulk:
			time_crystallization_estimate_bulk = val;
			return;
		case k_est_bulk:
			k_est_bulk = val;
			return;
		}
		throw new RuntimeException("Tag: " + tag.toString() + "and value: " + val + " is not yet set up.");
	}
	public String getTag(tags tag) {
		switch(tag) {
		case file_name:
			return file_name;
		case run_name:
			return run_name;
		case trip:
			return trip;
		case single_or_poly:
			return single_or_poly;
		case run_issues:
			return run_issues;
		case notebook_page_number:
			return notebook_page_number;
		case unwarped_or_raw:
			return unwarped_or_raw;
		case exists:
			return exists;
		case potential_issues:
			return potential_issues;
		case popcorn:
			return popcorn;
		case sample_cycle_index:
			return sample_cycle_index;
		case sample_number:
			return sample_number;
		case time_per_frame:
			return time_per_frame;
		case recorded_quench_frame:
			return recorded_quench_frame;
		case recorded_isotherm_frame:
			return recorded_isotherm_frame;
		case recorded_isotherm_time:
			return recorded_isotherm_time;
		case time_quench_start:
			return time_quench_start;
		case time_isotherm_reached:
			return time_isotherm_reached;
		case temperature_offset:
			return temperature_offset;
		case recorded_melt:
			return recorded_melt;
		case recorded_quench:
			return recorded_quench;
		case corrected_melt:
			return corrected_melt;
		case corrected_quench:
			return corrected_quench;
		case sample_volume:
			return sample_volume;
		case sample_size_in_cm:
			return sample_size_in_cm;
		case beam_size_in_cm:
			return beam_size_in_cm;
		case recorded_crystallization_frame:
			return recorded_crystallization_frame;
		case time_crystallization_estimate_bulk:
			return time_crystallization_estimate_bulk;
		case k_est_bulk:
			return k_est_bulk;
		}
		throw new RuntimeException("Tag: " + tag.toString() + " is not yet set up.");
	}
	// INSTANCE VARIABLES
	private String 
		original_line,
		file_name,
		run_name,
		trip,
		single_or_poly,
		run_issues,
		notebook_page_number,
		unwarped_or_raw,
		time_per_frame,
		recorded_quench_frame,
		recorded_isotherm_frame,
		recorded_isotherm_time,
		time_quench_start,
		time_isotherm_reached,
		temperature_offset,
		recorded_melt,
		recorded_quench,
		corrected_melt = null,
		corrected_quench = null,
		sample_volume,
		sample_size_in_cm,
		beam_size_in_cm,
		exists,
		potential_issues,
		popcorn,
		sample_cycle_index,
		sample_number,
		recorded_crystallization_frame,
		time_crystallization_estimate_bulk,
		k_est_bulk;
	// GETTERS
	public String getRecordedCrystallizationFrame() { return recorded_crystallization_frame; }
	public String getTimeCrystallizationEstimate() { return time_crystallization_estimate_bulk; }
	public String getKEst() { return k_est_bulk; }
	
	public String getFileName() { return file_name; }
	public String getRunName() { return run_name; }
	public String getTrip() { return trip; }
	public String getSingleOrPoly() { return single_or_poly; }
	public String getRunIssues() { return run_issues; }
	public String getNotebookPageNumber() { return notebook_page_number; }
	public String getUnwarpedOrRaw() { return unwarped_or_raw; }
	
	public String getOriginalLine() { return original_line; }
	
	public String exists() { return exists; }
	public String hasPotentialIssues() { return potential_issues; }
	public String hasPopcorn() { return popcorn; }

	public String getSampleCycleIndex() { return sample_cycle_index; }
	public String getSampleNumber() { return sample_number; }
	
	public String getTimePerFrame() { return time_per_frame; }
	public String getRecordedQuenchFrame() { return recorded_quench_frame; }
	public String getRecordedIsothermFrame() { return recorded_isotherm_frame; }
	public String getRecordedIsothermTime() { return recorded_isotherm_time; }
	public String getTimeQuenchStart() { return time_quench_start; }
	public String getTimeIsothermReached() { return time_isotherm_reached; }
	public String getTemperatureOffset() { return temperature_offset; }
	public String getRecordedMelt() { return recorded_melt; }
	public String getRecordedQuench() { return recorded_quench; }
	public String getCorrectedMelt() {  
			if(corrected_melt != null) { return corrected_melt; } 
			else { return recorded_melt + temperature_offset + ""; }
	}
	public String getCorrectedQuench() { 
		if(corrected_quench != null) { return corrected_quench; } 
		else { return recorded_quench + temperature_offset; }
	}
	public String getSampleVolume() { return sample_volume; }
	public String getSampleSizeInCM() { return sample_size_in_cm; }
	public String getBeamSizeInCM() { return beam_size_in_cm; }
	
	// SETTERS
	public void setRecordedCrystallizationFrame(String recorded_crystallization_frame) { this.recorded_crystallization_frame = recorded_crystallization_frame; }
	public void setTimeCrystallizationEstimate(String time_crystallization_estimate) { this.time_crystallization_estimate_bulk = time_crystallization_estimate; }
	public void setKEst(String k_est) { this.k_est_bulk = k_est; }
	public void setOriginalLine(String original_line) { this.original_line = original_line; }
	public void setFileName(String file_name) {this.file_name = file_name; }
	public void setRunName(String run_name) { this.run_name = run_name; }
	public void setTrip(String trip) { this.trip = trip; }
	public void setSingleOrPoly(String single_or_poly) { this.single_or_poly = single_or_poly; }
	public void setRunIssues(String run_issues) { this.run_issues = run_issues; }
	public void setNotebookPageNumber(String notebook_page_number) { this.notebook_page_number = notebook_page_number; }
	public void setUnwarpedOrRaw(String unwarped_or_raw) { this.unwarped_or_raw = unwarped_or_raw; }
	
	public void setExists(String exists) { this.exists = exists; }
	public void setPotentialIssues(String potential_issues) { this.potential_issues = potential_issues; }
	public void setPopcorn(String popcorn) { this.popcorn = popcorn; }

	public void setSampleCycleIndex(String sample_cycle_index) { this.sample_cycle_index = sample_cycle_index; }
	public void setSampleNumber(String sample_number) { this.sample_number = sample_number; }
	
	public void setTimePerFrame(String time_per_frame) { this.time_per_frame = time_per_frame; }
	public void setRecordedQuenchFrame(String recorded_quench_frame) { this.recorded_quench_frame = recorded_quench_frame; }
	public void setRecordedIsothermFrame(String recorded_isotherm_frame) { this.recorded_isotherm_frame = recorded_isotherm_frame; }
	public void setRecordedIsothermTime(String recorded_isotherm_time) { this.recorded_isotherm_time = recorded_isotherm_time; }
	public void setTimeQuenchStart(String time_quench_start) { this.time_quench_start = time_quench_start; }
	public void setTimeIsothermReached(String time_isotherm_reached) { this.time_isotherm_reached = time_isotherm_reached; }
	public void setTemperatureOffset(String temperature_offset) { this.temperature_offset = temperature_offset; }
	public void setRecordedMelt(String recorded_melt) { this.recorded_melt = recorded_melt; }
	public void setRecordedQuench(String recorded_quench) { this.recorded_quench = recorded_quench; }
	public void setCorrectedMelt(String corrected_melt) { this.corrected_melt = corrected_melt; }
	public void setCorrectedQuench(String corrected_quench) { this.corrected_quench = corrected_quench; }
	public void setSampleVolume(String sample_volume) { this.sample_volume = sample_volume; }
	public void setSampleSizeInCM(String sample_size_in_cm) { this.sample_size_in_cm = sample_size_in_cm; }
	public void setBeamSizeInCM(String beam_size_in_cm) { this.beam_size_in_cm = beam_size_in_cm; }
	
	public String toString() { return original_line; }
}
	

