package dev.helloworld.jdbc.service;

import dev.helloworld.jdbc.domain.Member;
import dev.helloworld.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;

    public void accountTransfer(String fromId, String toId, int money) throws SQLException {

        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        memberRepository.update(fromId, fromMember.getMoney() - money);
        validation(toId);
        memberRepository.update(toId, toMember.getMoney() + money);

    }

    private static void validation(String toId) {
        if(toId.equals("ex")){
            throw new IllegalStateException("이체 중 예외발생");
        }
    }
}
