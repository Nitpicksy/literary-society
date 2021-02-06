package nitpicksy.literarysociety2.repository;

import nitpicksy.literarysociety2.model.PDFDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDFDocumentRepository extends JpaRepository<PDFDocument, Long> {

    List<PDFDocument> findByBookIdOrderByCreatedDesc(Long id);
    
    PDFDocument findByName(String name);
}
