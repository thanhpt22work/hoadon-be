package com.hoadon.service;

import com.hoadon.dto.InvoiceDTO;
import com.hoadon.dto.InvoiceItemDTO;
import com.hoadon.entity.Customer;
import com.hoadon.entity.Invoice;
import com.hoadon.entity.InvoiceItem;
import com.hoadon.entity.InvoiceStatus;
import com.hoadon.repository.CustomerRepository;
import com.hoadon.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class InvoiceService {
    
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    
    public InvoiceDTO createInvoice(InvoiceDTO invoiceDTO) {
        Invoice invoice = new Invoice();
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setInvoiceDate(invoiceDTO.getInvoiceDate());
        
        invoice.setCompanyName(invoiceDTO.getCompanyName());
        invoice.setCompanyContact(invoiceDTO.getCompanyContact());
        invoice.setCompanyAddress(invoiceDTO.getCompanyAddress());
        invoice.setCompanyPhone(invoiceDTO.getCompanyPhone());
        invoice.setCompanyEmail(invoiceDTO.getCompanyEmail());
        invoice.setCompanyBank(invoiceDTO.getCompanyBank());
        invoice.setCompanyZalo(invoiceDTO.getCompanyZalo());
        
        Customer customer = resolveCustomer(invoiceDTO);
        invoice.setCustomer(customer);

        // Create items
        if (invoiceDTO.getItems() != null) {
            List<InvoiceItem> items = invoiceDTO.getItems().stream()
                    .map(itemDTO -> {
                        InvoiceItem item = new InvoiceItem();
                        item.setProductName(itemDTO.getProductName());
                        item.setUnit(itemDTO.getUnit());
                        item.setQuantity(itemDTO.getQuantity());
                        item.setUnitPrice(itemDTO.getUnitPrice());
                        item.setTotal(itemDTO.getQuantity().multiply(itemDTO.getUnitPrice()));
                        return item;
                    })
                    .collect(Collectors.toList());
            invoice.setItems(items);
        }
        
        invoice.setTotal(invoiceDTO.getTotal());
        invoice.setPaidAmount(invoiceDTO.getPaidAmount() != null ? invoiceDTO.getPaidAmount() : BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.valueOf(invoiceDTO.getStatus().toUpperCase()));
        
        Invoice saved = invoiceRepository.save(invoice);
        return convertToDTO(saved);
    }
    
    public InvoiceDTO updateInvoice(Long id, InvoiceDTO invoiceDTO) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        
        invoice.setInvoiceNumber(invoiceDTO.getInvoiceNumber());
        invoice.setInvoiceDate(invoiceDTO.getInvoiceDate());
        
        invoice.setCompanyName(invoiceDTO.getCompanyName());
        invoice.setCompanyContact(invoiceDTO.getCompanyContact());
        invoice.setCompanyAddress(invoiceDTO.getCompanyAddress());
        invoice.setCompanyPhone(invoiceDTO.getCompanyPhone());
        invoice.setCompanyEmail(invoiceDTO.getCompanyEmail());
        invoice.setCompanyBank(invoiceDTO.getCompanyBank());
        invoice.setCompanyZalo(invoiceDTO.getCompanyZalo());
        
        Customer customer = resolveCustomer(invoiceDTO);
        invoice.setCustomer(customer);

        // Update items
        if (invoiceDTO.getItems() != null) {
            List<InvoiceItem> newItems = invoiceDTO.getItems().stream()
                    .map(itemDTO -> {
                        InvoiceItem item = new InvoiceItem();
                        item.setProductName(itemDTO.getProductName());
                        item.setUnit(itemDTO.getUnit());
                        item.setQuantity(itemDTO.getQuantity());
                        item.setUnitPrice(itemDTO.getUnitPrice());
                        item.setTotal(itemDTO.getQuantity().multiply(itemDTO.getUnitPrice()));
                        return item;
                    })
                    .collect(Collectors.toList());
            invoice.getItems().clear();
            invoice.getItems().addAll(newItems);
        }
        
        invoice.setTotal(invoiceDTO.getTotal());
        invoice.setPaidAmount(invoiceDTO.getPaidAmount() != null ? invoiceDTO.getPaidAmount() : BigDecimal.ZERO);
        invoice.setStatus(InvoiceStatus.valueOf(invoiceDTO.getStatus().toUpperCase()));
        
        Invoice updated = invoiceRepository.save(invoice);
        return convertToDTO(updated);
    }
    
    public void deleteInvoice(Long id) {
        invoiceRepository.deleteById(id);
    }
    
    public InvoiceDTO getInvoiceById(Long id) {
        Invoice invoice = invoiceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Invoice not found"));
        return convertToDTO(invoice);
    }
    
    public Page<InvoiceDTO> getAllInvoices(Pageable pageable) {
        return invoiceRepository.findAll(pageable)
                .map(this::convertToDTO);
    }
    
    public Page<InvoiceDTO> getInvoicesByStatus(String status, Pageable pageable) {
        InvoiceStatus invoiceStatus = InvoiceStatus.valueOf(status.toUpperCase());
        return invoiceRepository.findByStatus(invoiceStatus, pageable)
                .map(this::convertToDTO);
    }
    
    public Page<InvoiceDTO> searchInvoices(String keyword, Pageable pageable) {
        return invoiceRepository.findByCustomer_NameContainingIgnoreCase(keyword, pageable)
                .map(this::convertToDTO);
    }

    public Page<InvoiceDTO> getAllInvoicesWithFilter(
            String status,
            String search,
            String fromDate,
            String toDate,
            int page,
            int limit,
            String sortBy,
            String sortOrder,
            Pageable pageable) {
        Pageable resolvedPageable = buildPageable(page, limit, sortBy, sortOrder, pageable);

        InvoiceStatus invoiceStatus = (status != null && !status.isBlank())
                ? InvoiceStatus.valueOf(status.toUpperCase()) : null;
        String statusParam = invoiceStatus != null ? invoiceStatus.name() : null;
        String searchParam = (search != null && !search.isBlank()) ? search : null;
        String fromParam = (fromDate != null && !fromDate.isBlank()) ? fromDate : null;
        String toParam = (toDate != null && !toDate.isBlank()) ? toDate : null;

        return invoiceRepository.findWithFilters(statusParam, searchParam, fromParam, toParam, resolvedPageable)
                .map(this::convertToDTO);
    }

    public InvoiceDTO getInvoiceById(String id) {
        return getInvoiceById(parseId(id));
    }

    public InvoiceDTO updateInvoice(String id, InvoiceDTO invoiceDTO) {
        return updateInvoice(parseId(id), invoiceDTO);
    }

    public void deleteInvoice(String id) {
        deleteInvoice(parseId(id));
    }

    public InvoiceDTO updateInvoiceStatus(String id, String status) {
        Invoice invoice = invoiceRepository.findById(parseId(id))
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        String normalized = status == null ? "" : status.replace("\"", "").trim();
        invoice.setStatus(InvoiceStatus.valueOf(normalized.toUpperCase()));
        Invoice updated = invoiceRepository.save(invoice);
        return convertToDTO(updated);
    }

    public String getNextInvoiceNumber() {
        List<String> numbers = invoiceRepository.findAllInvoiceNumbers();
        int maxSuffix = numbers.stream()
                .map(n -> n.replaceAll(".*-(\\d+)$", "$1"))
                .filter(n -> n.matches("\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(0);
        return String.format("HD-%03d", maxSuffix + 1);
    }

    public Object addPaymentToInvoice(String id, Object paymentRequest) {
        Invoice invoice = invoiceRepository.findById(parseId(id))
                .orElseThrow(() -> new RuntimeException("Invoice not found"));

        BigDecimal currentPaid = invoice.getPaidAmount() == null ? BigDecimal.ZERO : invoice.getPaidAmount();
        BigDecimal paymentAmount = extractPaymentAmount(paymentRequest);
        invoice.setPaidAmount(currentPaid.add(paymentAmount));

        if (invoice.getRemaining().compareTo(BigDecimal.ZERO) <= 0) {
            invoice.setStatus(InvoiceStatus.PAID);
        }

        Invoice updated = invoiceRepository.save(invoice);
        return convertToDTO(updated);
    }

    /**
     * Tìm customer theo SĐT. Nếu đã tồn tại thì dùng lại, chưa có thì tạo mới.
     */
    private Customer resolveCustomer(InvoiceDTO dto) {
        String phone = dto.getClientPhone();
        if (phone == null || phone.isBlank()) {
            throw new RuntimeException("Cần cung cấp số điện thoại khách hàng để tạo hóa đơn");
        }
        return customerRepository.findByPhone(phone.trim())
                .orElseGet(() -> {
                    Customer newCustomer = new Customer();
                    newCustomer.setName(dto.getClientName() != null ? dto.getClientName() : "");
                    newCustomer.setPhone(phone.trim());
                    newCustomer.setAddress(dto.getClientAddress());
                    newCustomer.setTaxCode(dto.getClientTaxCode());
                    return customerRepository.save(newCustomer);
                });
    }

    private Pageable buildPageable(int page, int limit, String sortBy, String sortOrder, Pageable pageable) {
        if (pageable != null && pageable.isPaged()) {
            return pageable;
        }

        int pageIndex = Math.max(page - 1, 0);
        int pageSize = limit > 0 ? limit : 20;
        String sortField = toSnakeCase((sortBy == null || sortBy.isBlank()) ? "createdAt" : sortBy);
        Sort.Direction direction = "asc".equalsIgnoreCase(sortOrder) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(pageIndex, pageSize, Sort.by(direction, sortField));
    }

    private String toSnakeCase(String camel) {
        return camel.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    private Long parseId(String id) {
        try {
            return Long.parseLong(id);
        } catch (NumberFormatException ex) {
            throw new RuntimeException("Invalid invoice id: " + id);
        }
    }

    private BigDecimal extractPaymentAmount(Object paymentRequest) {
        if (!(paymentRequest instanceof Map<?, ?> payload) || !payload.containsKey("amount")) {
            throw new RuntimeException("Payment request must contain 'amount'");
        }

        Object amountValue = payload.get("amount");
        if (amountValue == null) {
            throw new RuntimeException("Payment amount is required");
        }

        if (amountValue instanceof Number number) {
            return BigDecimal.valueOf(number.doubleValue());
        }

        return new BigDecimal(amountValue.toString());
    }
    
    private InvoiceDTO convertToDTO(Invoice invoice) {
        InvoiceDTO dto = new InvoiceDTO();
        dto.setId(invoice.getId());
        dto.setInvoiceNumber(invoice.getInvoiceNumber());
        dto.setInvoiceDate(invoice.getInvoiceDate());
        
        dto.setCompanyName(invoice.getCompanyName());
        dto.setCompanyContact(invoice.getCompanyContact());
        dto.setCompanyAddress(invoice.getCompanyAddress());
        dto.setCompanyPhone(invoice.getCompanyPhone());
        dto.setCompanyEmail(invoice.getCompanyEmail());
        dto.setCompanyBank(invoice.getCompanyBank());
        dto.setCompanyZalo(invoice.getCompanyZalo());

        if (invoice.getCustomer() != null) {
            Customer c = invoice.getCustomer();
            dto.setCustomerId(c.getId());
            dto.setClientName(c.getName());
            dto.setClientAddress(c.getAddress());
            dto.setClientPhone(c.getPhone());
            dto.setClientTaxCode(c.getTaxCode());
        }

        if (invoice.getItems() != null) {
            dto.setItems(invoice.getItems().stream()
                    .map(item -> {
                        InvoiceItemDTO itemDTO = new InvoiceItemDTO();
                        itemDTO.setId(item.getId());
                        itemDTO.setProductName(item.getProductName());
                        itemDTO.setUnit(item.getUnit());
                        itemDTO.setQuantity(item.getQuantity());
                        itemDTO.setUnitPrice(item.getUnitPrice());
                        itemDTO.setTotal(item.getTotal());
                        return itemDTO;
                    })
                    .collect(Collectors.toList()));
        }
        
        dto.setTotal(invoice.getTotal());
        dto.setPaidAmount(invoice.getPaidAmount());
        dto.setRemaining(invoice.getRemaining());
        dto.setStatus(invoice.getStatus().name().toLowerCase());
        
        dto.setCreatedAt(invoice.getCreatedAt());
        dto.setUpdatedAt(invoice.getUpdatedAt());
        
        return dto;
    }
}
