package com.example.concert.infrastructure.outbox;

import com.example.concert.domain.outBox.ReservationOutBoxWriter;
import com.example.concert.exption.BusinessBaseException;
import com.example.concert.exption.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@RequiredArgsConstructor
@Service
public class ReservationOutBoxImpl implements ReservationOutBoxWriter {

    private final ReservationOutBoxRepository outBoxRepository;
    @Override
    public void complete(ReservationOutBox outBox) {
        saveOutBox(ReservationOutBox.builder().id(outBox.getId()).message(outBox.getMessage()).outBoxStatus(ReservationOutBoxStatus.PUBLISHED).build());
    }

    @Override
    public void saveOutBox(ReservationOutBox outBox) {
        outBoxRepository.save(outBox);
    }

    @Override
    public List<ReservationOutBox> findOutBoxFailed(ReservationOutBoxStatus outBoxStatus) {
        return outBoxRepository.findOutBoxesByOutBoxStatus(outBoxStatus);
    }

    @Override
    public ReservationOutBox findById(Long id) {
        return outBoxRepository.findById(id).orElseThrow(()->new BusinessBaseException(ErrorCode.OUTBOX_NOT_FOUND));
    }
}
