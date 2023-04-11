package server.api;

import commons.CardList;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.query.FluentQuery;
import server.database.CardListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class TestCardListRepository implements CardListRepository {

    public final List<CardList> cardLists = new ArrayList<>();
    public final List<String> calledMethods = new ArrayList<>();

    private void call(String name) {
        calledMethods.add(name);
    }

    @Override
    public List<CardList> findAll() {
        calledMethods.add("findAll");
        return cardLists;
    }

    @Override
    public List<CardList> findAll(Sort sort) {
        return null;
    }

    @Override
    public Page<CardList> findAll(Pageable pageable) {
        return null;
    }

    @Override
    public List<CardList> findAllById(Iterable<Long> longs) {
        return null;
    }

    @Override
    public long count() {
        return cardLists.size();
    }

    @Override
    public void deleteById(Long aLong) {
        call("deleteById");
        for (int i = 0; i < cardLists.size(); i++) {
            if (cardLists.get(i).id == aLong) {
                cardLists.remove(i);
                return;
            }
        }
    }

    @Override
    public void delete(CardList entity) {

    }

    @Override
    public void deleteAllById(Iterable<? extends Long> longs) {

    }

    @Override
    public void deleteAll(Iterable<? extends CardList> entities) {

    }

    @Override
    public void deleteAll() {

    }

    @Override
    public <S extends CardList> S save(S entity) {
        call("save");

        for (int i = 0; i < cardLists.size(); i++) {
            if (cardLists.get(i).id == entity.id) {
                cardLists.set(i, entity);
                return entity;
            }
        }

        cardLists.add(entity);
        return entity;
    }

    @Override
    public <S extends CardList> List<S> saveAll(Iterable<S> entities) {
        return null;
    }

    @Override
    public Optional<CardList> findById(Long aLong) {
        call("findById");
        for (CardList cardList : cardLists) {
            if (cardList.id == aLong) {
                return Optional.of(cardList);
            }
        }
        return Optional.empty();
    }

    @Override
    public boolean existsById(Long id) {
        call("existsById");
        return cardLists.stream().anyMatch(q -> q.id == id);
    }

    @Override
    public void flush() {

    }

    @Override
    public <S extends CardList> S saveAndFlush(S entity) {
        return null;
    }

    @Override
    public <S extends CardList> List<S> saveAllAndFlush(Iterable<S> entities) {
        return null;
    }

    @Override
    public void deleteAllInBatch(Iterable<CardList> entities) {

    }

    @Override
    public void deleteAllByIdInBatch(Iterable<Long> longs) {

    }

    @Override
    public void deleteAllInBatch() {

    }

    @Override
    public CardList getOne(Long aLong) {
        return null;
    }

    @Override
    public CardList getById(Long aLong) {
        return null;
    }

    @Override
    public <S extends CardList> Optional<S> findOne(Example<S> example) {
        return Optional.empty();
    }

    @Override
    public <S extends CardList> List<S> findAll(Example<S> example) {
        return null;
    }

    @Override
    public <S extends CardList> List<S> findAll(Example<S> example, Sort sort) {
        return null;
    }

    @Override
    public <S extends CardList> Page<S> findAll(Example<S> example, Pageable pageable) {
        return null;
    }

    @Override
    public <S extends CardList> long count(Example<S> example) {
        return 0;
    }

    @Override
    public <S extends CardList> boolean exists(Example<S> example) {
        return false;
    }

    @Override
    public <S extends CardList, R> R findBy(
            Example<S> example,
            Function<FluentQuery.FetchableFluentQuery<S>, R> queryFunction) {
        return null;
    }
}


