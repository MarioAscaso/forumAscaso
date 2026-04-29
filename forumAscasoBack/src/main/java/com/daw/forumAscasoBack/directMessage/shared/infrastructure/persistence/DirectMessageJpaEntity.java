package com.daw.forumAscasoBack.directMessage.shared.infrastructure.persistence;

import com.daw.forumAscasoBack.user.shared.infrastructure.persistence.UserJpaEntity;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "direct_messages")
public class DirectMessageJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_id", nullable = false)
    private UserJpaEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "receiver_id", nullable = false)
    private UserJpaEntity receiver;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private LocalDateTime sentAt;

    @Column(nullable = false)
    private boolean ccReadBySuperadmin = false;

    public DirectMessageJpaEntity() {}

    public DirectMessageJpaEntity(UserJpaEntity sender, UserJpaEntity receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.sentAt = LocalDateTime.now();
        this.ccReadBySuperadmin = false;
    }

    public Long getId() { return id; }
    public UserJpaEntity getSender() { return sender; }
    public UserJpaEntity getReceiver() { return receiver; }
    public String getContent() { return content; }
    public LocalDateTime getSentAt() { return sentAt; }
    public boolean isCcReadBySuperadmin() { return ccReadBySuperadmin; }
    public void setCcReadBySuperadmin(boolean ccReadBySuperadmin) { this.ccReadBySuperadmin = ccReadBySuperadmin; }
}