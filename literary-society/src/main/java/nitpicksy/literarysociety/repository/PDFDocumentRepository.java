package nitpicksy.literarysociety.repository;

import nitpicksy.literarysociety.model.PDFDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PDFDocumentRepository extends JpaRepository<PDFDocument, Long> {

    List<PDFDocument> findByBookIdOrderByCreatedDesc(Long id);
    
    PDFDocument findByName(String name);
}
