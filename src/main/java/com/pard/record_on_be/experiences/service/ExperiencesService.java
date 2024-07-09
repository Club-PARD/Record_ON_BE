package com.pard.record_on_be.experiences.service;

import com.pard.record_on_be.answer_histories.entity.AnswerHistories;
import com.pard.record_on_be.answer_histories.repo.AnswerHistoriesRepo;
import com.pard.record_on_be.experiences.dto.ExperiencesDTO;
import com.pard.record_on_be.experiences.entity.Experiences;
import com.pard.record_on_be.experiences.repo.ExperiencesRepo;
import com.pard.record_on_be.project_data.entity.ProjectData;
import com.pard.record_on_be.project_data.repo.ProjectDataRepo;
import com.pard.record_on_be.projects.entity.Projects;
import com.pard.record_on_be.projects.repo.ProjectsRepo;
import com.pard.record_on_be.stored_info.entity.StoredQuestionInfo;
import com.pard.record_on_be.stored_info.entity.StoredTagInfo;
import com.pard.record_on_be.stored_info.repo.StoredQuestionInfoRepo;
import com.pard.record_on_be.stored_info.repo.StoredTagInfoRepo;
import com.pard.record_on_be.util.ResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.*;
import java.util.stream.Collectors;

@Service
public class ExperiencesService {

    private final ExperiencesRepo experiencesRepo;
    private final StoredQuestionInfoRepo storedQuestionInfoRepo;
    private final AnswerHistoriesRepo answerHistoriesRepo;
    private final ProjectsRepo projectsRepo;
    private final StoredTagInfoRepo storedTagInfoRepo;
    private final ProjectDataRepo projectDataRepo;

    public ExperiencesService(ExperiencesRepo experiencesRepo, StoredQuestionInfoRepo storedQuestionInfoRepo, AnswerHistoriesRepo answerHistoriesRepo, ProjectsRepo projectsRepo, StoredTagInfoRepo storedTagInfoRepo, ProjectDataRepo projectDataRepo) {
        this.experiencesRepo = experiencesRepo;
        this.storedQuestionInfoRepo = storedQuestionInfoRepo;
        this.answerHistoriesRepo = answerHistoriesRepo;
        this.projectsRepo = projectsRepo;
        this.storedTagInfoRepo = storedTagInfoRepo;
        this.projectDataRepo = projectDataRepo;
    }

    // 경험 생성
    @Transactional
    public ResponseDTO createExperience(ExperiencesDTO.ExperienceInfo experienceInfo) {
        try {
            // 대상 프로젝트의 존재여부 확인
            Projects projects = projectsRepo.findById(experienceInfo.getProjects_id())
                    .orElseThrow(() -> new NoSuchElementException("Project with ID " + experienceInfo.getProjects_id() + " not found"));

            // 태그와 질문의 존재여부 확인
            checkQuestionsAndTags(experienceInfo.getQuestion_ids(), experienceInfo.getTag_ids());

            // 경험 생성
            Experiences experience = Experiences.builder()
                    .user_id(experienceInfo.getUser_id())
                    .projects_id(experienceInfo.getProjects_id())
                    .title(experienceInfo.getTitle())
                    .exp_date(experienceInfo.getExp_date())
                    .update_date(new Date())
                    .free_content(experienceInfo.getFree_content())
                    .common_question_answer(experienceInfo.getCommon_question_answer())
                    .build();

            // 프로젝트 설정
            experience.setProjects(projects);
            experience = experiencesRepo.save(experience);

            // 답변 기록과 프로젝트 데이터 생성
            createOrUpdateAnswerHistories(experienceInfo, experience);
            createOrUpdateProjectData(experienceInfo, experience);

            return new ResponseDTO(true, "Experience created successfully", new ExperiencesDTO.Read(experience));
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return new ResponseDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(false, "An error occurred while creating the experience: " + e.getMessage());
        }
    }

    // 경험 상세보기
    public ResponseDTO getExperienceDetails(Integer experienceId) {
        try {
            // 대상 경험 존재여부 검사
            Experiences experience = experiencesRepo.findById(experienceId)
                    .orElseThrow(() -> new NoSuchElementException("Experience with ID " + experienceId + " not found"));

            // 답변 기록에서 경험에 해당하는 태그 id 가져오기
            List<Integer> tagIds = experience.getAnswerHistoriesList().stream()
                    .map(AnswerHistories::getTag_id)
                    .toList();

            // 가져온 태그 id 에 해당하는 태그 이름 가져오기
            List<String> tagNames = tagIds.stream()
                    .map(tagId -> storedTagInfoRepo.findById(tagId)
                            .orElseThrow(() -> new NoSuchElementException("Tag with ID " + tagId + " not found"))
                            .getTagName())
                    .toList();

            // 답변 기록에서 경험에 해당하는 질문 아이디 가져오기
            List<Integer> questionIds = experience.getAnswerHistoriesList().stream()
                    .map(AnswerHistories::getQuestion_id)
                    .toList();

            // 가져온 질문 id 에 해당하는 질문 내용 가져오기
            List<String> questionTexts = experience.getAnswerHistoriesList().stream()
                    .map(answerHistories -> storedQuestionInfoRepo
                            .findById(answerHistories.getQuestion_id())
                            .map(StoredQuestionInfo::getQuestionText)
                            .orElse("Unknown Question")
                    )
                    .toList();

            // 답변 기록에서 경험에 해당하는 답변 가져오기
            List<String> questionAnswers = experience.getAnswerHistoriesList().stream()
                    .map(AnswerHistories::getContent)
                    .toList();

            // 프로젝트 데이터에서 경험에 해당하는 참고 링크 가져오기
            List<String> referenceLinks = projectDataRepo.findAllByExperiencesId(experienceId).stream()
                    .map(ProjectData::getReferences_link)
                    .toList();

            // 경험 상세보기 DTO 생성
            ExperiencesDTO.ExperienceDetailsResponse response = new ExperiencesDTO.ExperienceDetailsResponse(
                    experience.getTitle(),
                    experience.getExp_date(),
                    tagIds,
                    tagNames,
                    experience.getFree_content(),
                    referenceLinks,
                    experience.getCommon_question_answer(),
                    questionIds,
                    questionTexts,
                    questionAnswers
            );

            return new ResponseDTO(true, "Experience details retrieved successfully", response);
        } catch (NoSuchElementException e) {
            return new ResponseDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(false, "An error occurred while fetching the experience details: " + e.getMessage());
        }
    }

    // 프론트에서 보내준 필터링 조건에 맞춘 데이터들을 보내주기
    public List<ExperiencesDTO.ExperienceSearchResponse> findExperiencesByFilter(ExperiencesDTO.ExperienceSearchRequest experienceSearchRequest) {
        List<ExperiencesDTO.ExperienceSearchResponse> experienceSearchResponseList;
        // project 의 experience 전체 탐색
        experienceSearchResponseList = findExperienceShortByProjectId(experienceSearchRequest.getProject_id());
        // 날짜 선택으로 1차 필터링
        experienceSearchResponseList = findExperienceShortByDate(experienceSearchRequest.getStart_date(), experienceSearchRequest.getFinish_date(), experienceSearchResponseList);
        // 태그 선택으로 2차 필터링
        experienceSearchResponseList = findExperiencesShortByTag(experienceSearchRequest.getTag_name(), experienceSearchResponseList);
        // 텍스트 검색으로 3차 필터링
        experienceSearchResponseList = findExperiencesShortByText(experienceSearchRequest.getSearch_text(), experienceSearchResponseList);
        return experienceSearchResponseList;
    }

    public List<ExperiencesDTO.ExperienceSearchResponse> findExperiencesShortByText(String text, List<ExperiencesDTO.ExperienceSearchResponse> experienceSearchResponseList) {
        // 1차 필터링: answerHistories 에서 text 를 포함하는 experiences 찾기
        List<ExperiencesDTO.ExperienceSearchResponse> expFilteredList = experienceSearchResponseList.stream()
                .filter(experienceSearchResponse -> {
                    Optional<Experiences> experiencesOptional = experiencesRepo.findById(experienceSearchResponse.getExperience_id());
                    if (experiencesOptional.isPresent()) {
                        Experiences experiences = experiencesOptional.get();
                        return experiences.getAnswerHistoriesList().stream()
                                .anyMatch(answerHistories -> answerHistories.getContent().contains(text));
                    }
                    return false;
                }).collect(Collectors.toList());

        Set<Integer> filteredExperienceIds = expFilteredList.stream()
                .map(ExperiencesDTO.ExperienceSearchResponse::getExperience_id)
                .collect(Collectors.toSet());

        // 2차 필터링: 제목에서 필터링
        experienceSearchResponseList.stream().filter(
                experienceSearchResponse -> {
                    Integer experienceId = experienceSearchResponse.getExperience_id();
                    if (!filteredExperienceIds.contains(experienceId)) {
                        Optional<Experiences> SecondFilteredOptional = experiencesRepo.findById(experienceId);
                        if (SecondFilteredOptional.isPresent()) {
                            Experiences experiences = SecondFilteredOptional.get();
                            return experiences.getTitle().contains(text);
                        }
                    }
                    return false;
                }
        ).forEach(experienceSearchResponse -> {
            filteredExperienceIds.add(experienceSearchResponse.getExperience_id());
            expFilteredList.add(experienceSearchResponse);
        });

        // 3차 필터링: 공통 질문란에서 필터링
        experienceSearchResponseList.stream().filter(
                experienceSearchResponse -> {
                    Integer experienceId = experienceSearchResponse.getExperience_id();
                    if (!filteredExperienceIds.contains(experienceId)) {
                        Optional<Experiences> SecondFilteredOptional = experiencesRepo.findById(experienceId);
                        if (SecondFilteredOptional.isPresent()) {
                            Experiences experiences = SecondFilteredOptional.get();
                            return experiences.getCommon_question_answer().contains(text);
                        }
                    }
                    return false;
                }
        ).forEach(experienceSearchResponse -> {
            filteredExperienceIds.add(experienceSearchResponse.getExperience_id());
            expFilteredList.add(experienceSearchResponse);
        });

        // 4차 필터링: free_content 에서 text 를 포함하는 experiences 찾기, 이미 추가된 experience_id는 제외
        experienceSearchResponseList.stream().filter(
                experienceSearchResponse -> {
                    Integer experienceId = experienceSearchResponse.getExperience_id();
                    if (!filteredExperienceIds.contains(experienceId)) {
                        Optional<Experiences> finalFilteredOptional = experiencesRepo.findById(experienceId);
                        if (finalFilteredOptional.isPresent()) {
                            Experiences experiences = finalFilteredOptional.get();
                            return experiences.getFree_content().contains(text);
                        }
                    }
                    return false;
                }).forEach(experienceSearchResponse -> {
            filteredExperienceIds.add(experienceSearchResponse.getExperience_id());
            expFilteredList.add(experienceSearchResponse);
        });

        return expFilteredList;
    }

    // 보내준 experience 들 중에 tag 가 전부 들어간 것들을 전부 찾기
    public List<ExperiencesDTO.ExperienceSearchResponse> findExperiencesShortByTag(List<String> tagNameList, List<ExperiencesDTO.ExperienceSearchResponse> experienceSearchResponseList) {
        if (tagNameList == null || tagNameList.isEmpty()) {
            return experienceSearchResponseList;
        }
        return experienceSearchResponseList.stream().filter(
                experienceSearchResponse -> experienceSearchResponse.getTag_name().containsAll(tagNameList)
        ).toList();
    }

    // 프로젝트 ID 로 해당하는 경험들 간략하게 가져오기
    public List<ExperiencesDTO.ExperienceSearchResponse> findExperienceShortByProjectId(Integer projectId) {
        List<ExperiencesDTO.ExperienceSearchResponse> readDefaultPageList = new ArrayList<>();
        Projects projects = projectsRepo.findById(projectId).orElseThrow(() -> new RuntimeException("Project not found"));

        projects.getExperiencesList().forEach(experience -> {
            List<AnswerHistories> answerHistoriesList = experience.getAnswerHistoriesList();
            List<Integer> tagIds = findExperiencesTagId(answerHistoriesList);
            List<String> tagNames = findExperiencesTagName(tagIds);

            readDefaultPageList.add(
                    new ExperiencesDTO.ExperienceSearchResponse(
                            experience.getId(),
                            experience.getTitle(),
                            tagIds,
                            tagNames,
                            experience.getExp_date()
                    ));
        });
        return readDefaultPageList;
    }

    // AnswerHistories 에서 tag_id만 뽑아와서 List 로 만들어주는 메서드
    public List<Integer> findExperiencesTagId(List<AnswerHistories> answerHistoriesList) {
        List<Integer> experiencesTagIdList = new ArrayList<>();
        answerHistoriesList.forEach(answerHistories -> experiencesTagIdList.add(answerHistories.getTag_id()));
        return experiencesTagIdList;
    }

    // Tag ID 목록을 사용하여 Tag 이름 목록을 가져오는 메서드
    public List<String> findExperiencesTagName(List<Integer> tagIds) {
        List<String> experiencesTagNameList = new ArrayList<>();
        tagIds.forEach(tagId -> {
            StoredTagInfo tag = storedTagInfoRepo.findById(tagId).orElseThrow(() -> new RuntimeException("Tag not found"));
            experiencesTagNameList.add(tag.getTagName());
        });
        return experiencesTagNameList;
    }

    // 날짜 기준으로 sort 하는 list 만들어주기
    public List<ExperiencesDTO.ExperienceSearchResponse> findExperienceShortByDate(Date start_date, Date end_date, List<ExperiencesDTO.ExperienceSearchResponse> experienceSearchResponseList) {
        if(start_date == null && end_date == null){ return experienceSearchResponseList; }
        else if(end_date == null){
            return experienceSearchResponseList.stream().filter(
                    experienceSearchResponse -> experienceSearchResponse.getExp_date().after(start_date)
            ).toList();
        } else if (start_date == null) {
            return experienceSearchResponseList.stream().filter(
                    experienceSearchResponse -> experienceSearchResponse.getExp_date().before(end_date)
            ).toList();
        } else {
            return experienceSearchResponseList.stream().filter(
                    experienceSearchResponse -> experienceSearchResponse.getExp_date().before(end_date) && experienceSearchResponse.getExp_date().after(start_date)
            ).toList();
        }
    }

    public Object findAllExpCollectionPage(Integer project_id, UUID user_id) {
        try {
            Projects project = projectsRepo.findById(project_id)
                    .orElseThrow(() -> new NoSuchElementException("Project with ID " + project_id + " not found"));

            // Check if the user is the owner of the project
            if (!project.getUser_id().equals(user_id)) {
                return new ResponseDTO(false, "User is not authorized to view this project");
            }

            return new ExperiencesDTO.ExperiencesCollectionPageResponse(
                    project.getName(),
                    project.getPicture(),
                    project.getIs_finished(),
                    project.getStart_date(),
                    project.getFinish_date(),
                    project.getDescription(),
                    project.getPart(),
                    findExperienceShortByProjectId(project_id)
            );
        } catch (NoSuchElementException e) {
            return new ResponseDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(false, "An error occurred while fetching the project: " + e.getMessage());
        }
    }

    // 경험 업데이트
    @Transactional
    public ResponseDTO updateExperience(Integer experienceId, ExperiencesDTO.ExperienceInfo experienceInfo) {
        try {
            Optional<Experiences> optionalExperience = experiencesRepo.findById(experienceId);
            // 대상 경험의 존재여부 확인
            if (optionalExperience.isPresent()) {
                // 기존 경험 가져오기
                Experiences existingExperience = optionalExperience.get();
                Projects projects = projectsRepo.findById(experienceInfo.getProjects_id())
                        .orElseThrow(() -> new IllegalArgumentException("Project with ID " + experienceInfo.getProjects_id() + " not found"));

                // user id 가 기존 경험 생성한 user id 와 같은지 확인
                if (!existingExperience.getUser_id().equals(experienceInfo.getUser_id())) {
                    return new ResponseDTO(false, "User is not authorized to update this experience");
                }

                // 태그와 질문의 존재여부 확인
                checkQuestionsAndTags(experienceInfo.getQuestion_ids(), experienceInfo.getTag_ids());

                // 수정된 경험 생성
                Experiences updatedExperience = Experiences.builder()
                        .id(existingExperience.getId())
                        .user_id(existingExperience.getUser_id())
                        .projects_id(experienceInfo.getProjects_id())
                        .title(experienceInfo.getTitle())
                        .exp_date(experienceInfo.getExp_date())
                        .update_date(new Date())
                        .free_content(experienceInfo.getFree_content())
                        .common_question_answer(experienceInfo.getCommon_question_answer())
                        .projects(projects)
                        .build();

                // 답변 기록 수정
                Experiences finalUpdatedExperience = updatedExperience;
                List<AnswerHistories> answerHistoriesList = experienceInfo.getQuestion_ids().stream()
                        .map(questionId -> {
                            int index = experienceInfo.getQuestion_ids().indexOf(questionId);
                            String answer = experienceInfo.getQuestion_answers().get(index);
                            Integer tagId = experienceInfo.getTag_ids().get(index);
                            StoredQuestionInfo question = storedQuestionInfoRepo.findById(questionId).orElseThrow();

                            return AnswerHistories.builder()
                                    .question_id(questionId)
                                    .tag_id(tagId)
                                    .experience_id(finalUpdatedExperience.getId())
                                    .content(answer)
                                    .experiences(finalUpdatedExperience)
                                    .build();
                        }).toList();
                updatedExperience.setAnswerHistoriesList(answerHistoriesList);

                updatedExperience = experiencesRepo.save(updatedExperience);

                // 프로젝트 데이터 수정
                projectDataRepo.deleteByExperiencesId(updatedExperience.getId());
                Experiences finalUpdatedExperience1 = updatedExperience;
                List<ProjectData> projectDataList = experienceInfo.getReference_links().stream()
                        .map(link -> ProjectData.builder()
                                .experiencesId(finalUpdatedExperience1.getId())
                                .resources_type(1) // Assuming 1 is for reference links, adjust as needed
                                .references_link(link)
                                .projects(projects)
                                .build())
                        .toList();
                projectDataRepo.saveAll(projectDataList);

                return new ResponseDTO(true, "Experience updated successfully", new ExperiencesDTO.Read(updatedExperience));
            } else {
                return new ResponseDTO(false, "Experience not found");
            }
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return new ResponseDTO(false, e.getMessage());
        } catch (Exception e) {
            return new ResponseDTO(false, "An error occurred while updating the experience: " + e.getMessage());
        }
    }

    private void createOrUpdateAnswerHistories(ExperiencesDTO.ExperienceInfo experienceInfo, Experiences experience) {
        for (int i = 0; i < experienceInfo.getTag_ids().size(); i++) {
            Integer questionId = experienceInfo.getQuestion_ids().get(i);
            String answer = experienceInfo.getQuestion_answers().get(i);
            Integer tagId = experienceInfo.getTag_ids().get(i);

            AnswerHistories answerHistories = AnswerHistories.builder()
                    .question_id(questionId)
                    .tag_id(tagId)
                    .experience_id(experience.getId())
                    .content(answer)
                    .experiences(experience)
                    .build();
            answerHistoriesRepo.save(answerHistories);
        }
    }

    private void createOrUpdateProjectData(ExperiencesDTO.ExperienceInfo experienceInfo, Experiences experience) {
        List<ProjectData> projectDataList = experienceInfo.getReference_links().stream()
                .map(link -> ProjectData.builder()
                        .experiencesId(experience.getId())
                        .resources_type(1) // assuming 1 for references
                        .references_link(link)
                        .projects(experience.getProjects())
                        .build())
                .toList();

        projectDataRepo.saveAll(projectDataList);
    }

    // 주어진 태그와 질문의 관계를 확인하는 메서드
    public void checkQuestionsAndTags(List<Integer> questionIds, List<Integer> tagIds) {
        if (questionIds.size() != tagIds.size()) {
            throw new IllegalArgumentException("The number of questions and tags must be the same");
        }

        for (int i = 0; i < questionIds.size(); i++) {
            Integer questionId = questionIds.get(i);
            Integer tagId = tagIds.get(i);

            // 질문이 존재하는지 확인
            StoredQuestionInfo question = storedQuestionInfoRepo.findById(questionId)
                    .orElseThrow(() -> new NoSuchElementException("Question with ID " + questionId + " not found"));

            // 태그가 존재하는지 확인
            StoredTagInfo tag = storedTagInfoRepo.findById(tagId)
                    .orElseThrow(() -> new NoSuchElementException("Tag with ID " + tagId + " not found"));

            // 질문과 태그가 관련이 있는지 확인
            if (!question.getStoredTagInfo().getId().equals(tag.getId())) {
                throw new IllegalArgumentException("Question with ID " + questionId + " is not related to Tag with ID " + tagId);
            }
        }
    }

    @Transactional
    public ResponseDTO deleteExperience(Integer id, UUID userId) {
        Optional<Experiences> optionalExperience = experiencesRepo.findById(id);
        if (optionalExperience.isPresent()) {
            Experiences experience = optionalExperience.get();

            // Check if the user is the owner of the experience
            if (!experience.getUser_id().equals(userId)) {
                return new ResponseDTO(false, "User is not authorized to delete this experience");
            }

            // Delete related entities
            answerHistoriesRepo.deleteByExperiencesId(id);
            projectDataRepo.deleteByExperiencesId(id);
            experiencesRepo.delete(experience);

            return new ResponseDTO(true, "Experience deleted successfully");
        } else {
            return new ResponseDTO(false, "Experience not found");
        }
    }
}