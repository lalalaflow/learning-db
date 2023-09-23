package dev.helloworld.jdbc.service;

import dev.helloworld.jdbc.domain.Member;
import dev.helloworld.jdbc.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

/**
 * 트랜잭션 - 트랜잭션 AOP
 *
 */
@Slf4j
public class MemberServiceV4 {

    private final MemberRepository memberRepository;

    public MemberServiceV4(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void accountTransfer(String fromId, String toId, int money) {

        bizLogic(fromId, toId, money);
    }

    private void bizLogic(String fromId, String toId, int money) {
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
