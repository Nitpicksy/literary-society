package nitpicksy.literarysociety2.service;

import nitpicksy.literarysociety2.model.OpinionOfCommitteeMemberAboutComplaint;

import java.util.List;

public interface OpinionOfCommitteeMemberAboutComplaintService {

    List<OpinionOfCommitteeMemberAboutComplaint> findUnreviewedOpinions(Long plagiarismId, boolean reviewed);
}
