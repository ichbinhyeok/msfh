package owner.mysafefloridahome.vendor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import owner.mysafefloridahome.AppProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class OpeningProtectionOfficePresetService {

    private static final String PRESETS_HEADER =
            "preset_id,created_at,preset_name,office_label,sender_name,reply_instructions,service_area_note,permit_handling_note,attached_scope_note,boundary_scope_note";

    private final Path presetsPath;

    public OpeningProtectionOfficePresetService(AppProperties appProperties) {
        this.presetsPath = Path.of(appProperties.getStorage().getVendorPresetsPath());
    }

    public synchronized OpeningProtectionOfficePresetSaveResult save(OpeningProtectionOfficePresetRequest request) {
        List<OpeningProtectionOfficePresetRecord> existingRecords = new ArrayList<>(readRows(presetsPath).stream()
                .map(this::toRecord)
                .toList());
        String requestedPresetId = normalize(request.getPresetId(), "");
        Optional<OpeningProtectionOfficePresetRecord> existingRecord = requestedPresetId.isBlank()
                ? Optional.empty()
                : existingRecords.stream()
                        .filter(record -> record.presetId().equals(requestedPresetId))
                        .findFirst();
        String presetId = existingRecord.map(OpeningProtectionOfficePresetRecord::presetId)
                .orElseGet(() -> UUID.randomUUID().toString());
        OpeningProtectionOfficePresetRecord record = new OpeningProtectionOfficePresetRecord(
                presetId,
                existingRecord.map(OpeningProtectionOfficePresetRecord::createdAt).orElseGet(OffsetDateTime::now),
                normalize(request.getPresetName(), normalize(request.getOfficeLabel(), "Office preset")),
                normalize(request.getOfficeLabel(), "Opening protection intake desk"),
                normalize(request.getSenderName(), ""),
                normalize(request.getReplyInstructions(), ""),
                normalize(request.getServiceAreaNote(), ""),
                normalize(request.getPermitHandlingNote(), "Permit and inspection handling still need to be confirmed before signing."),
                normalize(request.getAttachedScopeNote(), ""),
                normalize(request.getBoundaryScopeNote(), ""));
        existingRecord.ifPresent(found -> existingRecords.removeIf(saved -> saved.presetId().equals(found.presetId())));
        existingRecords.add(record);
        writeAll(existingRecords);
        return new OpeningProtectionOfficePresetSaveResult(record, existingRecord.isPresent());
    }

    public Optional<OpeningProtectionOfficePresetRecord> findById(String presetId) {
        String normalizedPresetId = normalize(presetId, "");
        if (normalizedPresetId.isBlank()) {
            return Optional.empty();
        }
        return readRows(presetsPath).stream()
                .map(this::toRecord)
                .filter(record -> record.presetId().equals(normalizedPresetId))
                .findFirst();
    }

    public List<OpeningProtectionOfficePresetRecord> listRecent(int limit) {
        return readRows(presetsPath).stream()
                .map(this::toRecord)
                .sorted(Comparator.comparing(OpeningProtectionOfficePresetRecord::createdAt).reversed())
                .limit(limit)
                .toList();
    }

    public synchronized boolean delete(String presetId) {
        String normalizedPresetId = normalize(presetId, "");
        if (normalizedPresetId.isBlank()) {
            return false;
        }
        List<OpeningProtectionOfficePresetRecord> existingRecords = new ArrayList<>(readRows(presetsPath).stream()
                .map(this::toRecord)
                .toList());
        boolean removed = existingRecords.removeIf(record -> record.presetId().equals(normalizedPresetId));
        if (!removed) {
            return false;
        }
        writeAll(existingRecords);
        return true;
    }

    public String officePresetPagePath(String presetId) {
        return UriComponentsBuilder.fromPath("/vendor-packets/opening-protection/office-preset/")
                .queryParam("preset", presetId)
                .build()
                .encode()
                .toUriString();
    }

    public String packetBuilderPath(OpeningProtectionOfficePresetRecord record) {
        return buildLaunchPath("/tools/opening-protection/quote-prep-brief/build/", record);
    }

    public String estimatorPath(OpeningProtectionOfficePresetRecord record) {
        return buildLaunchPath("/vendor-packets/opening-protection/estimator-handoff/", record);
    }

    public String quoteBoundaryPath(OpeningProtectionOfficePresetRecord record) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath("/vendor-packets/opening-protection/quote-boundary/")
                .queryParam("permitHandlingMode", "confirm")
                .queryParam("attachedCaution", true)
                .queryParam("excludeOtherWork", true);
        appendPresetParams(builder, record);
        return builder.build().encode().toUriString();
    }

    private String buildLaunchPath(String path, OpeningProtectionOfficePresetRecord record) {
        UriComponentsBuilder builder = UriComponentsBuilder.fromPath(path);
        appendPresetParams(builder, record);
        return builder.build().encode().toUriString();
    }

    private void appendPresetParams(UriComponentsBuilder builder, OpeningProtectionOfficePresetRecord record) {
        appendIfNotBlank(builder, "officeLabel", record.officeLabel());
        appendIfNotBlank(builder, "senderName", record.senderName());
        appendIfNotBlank(builder, "replyInstructions", record.replyInstructions());
        appendIfNotBlank(builder, "serviceAreaNote", record.serviceAreaNote());
        appendIfNotBlank(builder, "permitHandlingNote", record.permitHandlingNote());
        appendIfNotBlank(builder, "attachedScopeNote", record.attachedScopeNote());
        appendIfNotBlank(builder, "boundaryScopeNote", record.boundaryScopeNote());
    }

    private OpeningProtectionOfficePresetRecord toRecord(String[] row) {
        return new OpeningProtectionOfficePresetRecord(
                column(row, 0),
                OffsetDateTime.parse(column(row, 1)),
                column(row, 2),
                column(row, 3),
                column(row, 4),
                column(row, 5),
                column(row, 6),
                column(row, 7),
                column(row, 8),
                column(row, 9));
    }

    private void ensureFile(Path path, String header) {
        try {
            Path parent = path.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            if (!Files.exists(path)) {
                Files.writeString(path, header + System.lineSeparator(), StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE_NEW);
                return;
            }
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            if (lines.isEmpty()) {
                Files.writeString(path, header + System.lineSeparator(), StandardCharsets.UTF_8,
                        StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
                return;
            }
            if (!header.equals(lines.getFirst())) {
                Path legacyPath = legacyBackupPath(path);
                Files.move(path, legacyPath, StandardCopyOption.REPLACE_EXISTING);
                Files.writeString(path, header + System.lineSeparator(), StandardCharsets.UTF_8,
                        StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
            }
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to prepare preset storage file " + path, exception);
        }
    }

    private Path legacyBackupPath(Path path) {
        String fileName = path.getFileName().toString();
        int extensionIndex = fileName.lastIndexOf('.');
        String baseName = extensionIndex >= 0 ? fileName.substring(0, extensionIndex) : fileName;
        String extension = extensionIndex >= 0 ? fileName.substring(extensionIndex) : "";
        String legacyName = baseName + ".legacy-" + System.currentTimeMillis() + extension;
        return path.resolveSibling(legacyName);
    }

    private void appendRow(Path path, String row) {
        try {
            Files.writeString(path, row + System.lineSeparator(), StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to append preset storage file " + path, exception);
        }
    }

    private void writeAll(List<OpeningProtectionOfficePresetRecord> records) {
        ensureFile(presetsPath, PRESETS_HEADER);
        StringBuilder builder = new StringBuilder(PRESETS_HEADER).append(System.lineSeparator());
        records.stream()
                .sorted(Comparator.comparing(OpeningProtectionOfficePresetRecord::createdAt).reversed())
                .forEach(record -> builder.append(csvRow(
                        sanitize(record.presetId()),
                        sanitize(record.createdAt().toString()),
                        sanitize(record.presetName()),
                        sanitize(record.officeLabel()),
                        sanitize(record.senderName()),
                        sanitize(record.replyInstructions()),
                        sanitize(record.serviceAreaNote()),
                        sanitize(record.permitHandlingNote()),
                        sanitize(record.attachedScopeNote()),
                        sanitize(record.boundaryScopeNote())))
                        .append(System.lineSeparator()));
        try {
            Files.writeString(presetsPath, builder.toString(), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING);
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to rewrite preset storage file " + presetsPath, exception);
        }
    }

    private List<String[]> readRows(Path path) {
        if (!Files.exists(path)) {
            return List.of();
        }
        try {
            return Files.readAllLines(path, StandardCharsets.UTF_8).stream()
                    .skip(1)
                    .filter(line -> !line.isBlank())
                    .map(this::parseCsvLine)
                    .toList();
        } catch (IOException exception) {
            throw new IllegalStateException("Failed to read preset storage file " + path, exception);
        }
    }

    private String column(String[] row, int index) {
        return index < row.length ? row[index] : "";
    }

    private void appendIfNotBlank(UriComponentsBuilder builder, String key, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        builder.queryParam(key, value);
    }

    private String normalize(String value, String fallback) {
        if (value == null) {
            return fallback;
        }
        String normalized = value.trim();
        return normalized.isBlank() ? fallback : normalized;
    }

    private String csvRow(String... values) {
        StringBuilder builder = new StringBuilder();
        for (int index = 0; index < values.length; index++) {
            if (index > 0) {
                builder.append(',');
            }
            builder.append(csvValue(values[index]));
        }
        return builder.toString();
    }

    private String csvValue(String value) {
        String normalized = value == null ? "" : value;
        return "\"" + normalized.replace("\"", "\"\"") + "\"";
    }

    private String[] parseCsvLine(String line) {
        List<String> columns = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;

        for (int index = 0; index < line.length(); index++) {
            char character = line.charAt(index);
            if (character == '"') {
                if (inQuotes && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    current.append('"');
                    index++;
                    continue;
                }
                inQuotes = !inQuotes;
                continue;
            }
            if (character == ',' && !inQuotes) {
                columns.add(current.toString());
                current.setLength(0);
                continue;
            }
            current.append(character);
        }

        columns.add(current.toString());
        return columns.toArray(String[]::new);
    }

    private String sanitize(String value) {
        if (value == null) {
            return "";
        }
        String sanitized = value.replace("\r", " ").replace("\n", " ").trim();
        if (sanitized.isEmpty()) {
            return sanitized;
        }
        char firstCharacter = sanitized.charAt(0);
        if (firstCharacter == '=' || firstCharacter == '+' || firstCharacter == '-'
                || firstCharacter == '@') {
            return "'" + sanitized;
        }
        return sanitized;
    }
}
