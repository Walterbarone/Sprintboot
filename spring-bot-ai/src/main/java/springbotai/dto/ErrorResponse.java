package springbotai.dto;

public record ErrorResponse(
        int status,
        String message
) {
}