package nitpicksy.literarysociety.service;

import nitpicksy.literarysociety.model.OpinionOfCommitteeMemberAboutComplaint;

import java.util.List;

public interface OpinionOfCommitteeMemberAboutComplaintService {

    List<OpinionOfCommitteeMemberAboutComplaint> findUnreviewedOpinions(Long plagiarismId, boolean reviewed);
}
