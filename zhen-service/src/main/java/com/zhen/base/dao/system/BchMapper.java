package com.zhen.base.dao.system;

import com.zhen.base.domain.system.Bch;

public interface BchMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table S_BCH
     *
     * @mbg.generated Wed Aug 08 17:56:59 CST 2018
     */
    int deleteByPrimaryKey(String bchCde);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table S_BCH
     *
     * @mbg.generated Wed Aug 08 17:56:59 CST 2018
     */
    int insert(Bch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table S_BCH
     *
     * @mbg.generated Wed Aug 08 17:56:59 CST 2018
     */
    int insertSelective(Bch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table S_BCH
     *
     * @mbg.generated Wed Aug 08 17:56:59 CST 2018
     */
    Bch selectByPrimaryKey(String bchCde);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table S_BCH
     *
     * @mbg.generated Wed Aug 08 17:56:59 CST 2018
     */
    int updateByPrimaryKeySelective(Bch record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table S_BCH
     *
     * @mbg.generated Wed Aug 08 17:56:59 CST 2018
     */
    int updateByPrimaryKey(Bch record);
}