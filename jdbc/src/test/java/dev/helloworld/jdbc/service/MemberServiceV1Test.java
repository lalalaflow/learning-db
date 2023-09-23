package dev.helloworld.jdbc.service;

import dev.helloworld.jdbc.connection.ConnectionConst;
import dev.helloworld.jdbc.domain.Member;
import dev.helloworld.jdbc.repository.MemberRepositoryV1;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

import java.sql.SQLException;

import static dev.helloworld.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class MemberServiceV1Test {

    private static final String MEMBER_A = "memberA";
    private static final String MEMBER_B = "memberB";

    private static final String MEMBER_EX = "ex";

    MemberRepositoryV1 memberRepository;
    MemberServiceV1 memberServiceV1;

    @BeforeEach
    void before() {
        DataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
        memberRepository = new MemberRepositoryV1(dataSource);
        memberServiceV1 = new MemberServiceV1(memberRepository);
    }

    @AfterEach
    void after() throws SQLException {
        memberRepository.delete(MEMBER_A);
        memberRepository.delete(MEMBER_B);
        memberRepository.delete(MEMBER_EX);
    }

    @Test
    @DisplayName("정상이체")
    void accountTransfer() throws SQLException {
        //given
        Member memberA = memberRepository.save(new Member(MEMBER_A, 10000));
        Member memberB = memberRepository.save(new Member(MEMBER_B, 10000));

        //when
        memberServiceV1.accountTransfer(memberA.getMemberId(), memberB.getMemberId(), 2000);

        //then
        Member fromMember = memberRepository.findById(memberA.getMemberId());
        Member toMember = memberRepository.findById(memberB.getMemberId());
        assertThat(fromMember.getMoney()).isEqualTo(8000);
        assertThat(toMember.getMoney()).isEqualTo(12000);
    }

    @Test
    @DisplayName("이체 시 예외발생")
    void accountTransferException() throws SQLException {
        // given
        Member memberA = memberRepository.save(new Member(MEMBER_A, 10000));
        Member memberEx = memberRepository.save(new Member(MEMBER_EX, 10000));

        // when
        assertThatThrownBy(() -> memberServiceV1.accountTransfer(memberA.getMemberId(), memberEx.getMemberId(), 2000))
                .isInstanceOf(IllegalStateException.class);

        // then
        Member findMemberA = memberRepository.findById(memberA.getMemberId());
        Member findMemberEx = memberRepository.findById(memberEx.getMemberId());
        assertThat(findMemberA.getMoney()).isEqualTo(8000);
        assertThat(findMemberEx.getMoney()).isEqualTo(10000);
    }
}