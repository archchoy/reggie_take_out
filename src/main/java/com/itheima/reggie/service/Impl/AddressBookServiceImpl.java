package com.itheima.reggie.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.itheima.reggie.common.BaseContext;
import com.itheima.reggie.common.CommonResult;
import com.itheima.reggie.entity.AddressBook;
import com.itheima.reggie.exception.CustomException;
import com.itheima.reggie.mapper.AddressBookMapper;
import com.itheima.reggie.service.AddressBookService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
    @Override
    public CommonResult<String> saveAddress(AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        baseMapper.insert(addressBook);
        return CommonResult.success("添加成功");
    }

    @Override
    public CommonResult<List<AddressBook>> listAddressBooks() {
        LambdaQueryWrapper<AddressBook> listAddressBookWrapper = new LambdaQueryWrapper<>();
        listAddressBookWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        return CommonResult.success(baseMapper.selectList(listAddressBookWrapper));
    }

    @Override
    public CommonResult<AddressBook> queryAddressById(Long id) {
        return CommonResult.success(baseMapper.selectById(id));
    }

    @Override
    public CommonResult<String> removeAddress(Long id) {

        // 如果是默认地址则不能删除
        LambdaQueryWrapper<AddressBook> query = new LambdaQueryWrapper<>();
        query.eq(AddressBook::getIsDefault,1).eq(AddressBook::getUserId,BaseContext.getCurrentId()).eq(AddressBook::getId,id);
        AddressBook addressBook = baseMapper.selectOne(query);
        if (addressBook != null){
            throw new  CustomException("不能删除默认地址!");
        }
        LambdaQueryWrapper<AddressBook> deleteWrapper = new LambdaQueryWrapper<>();
        deleteWrapper.eq(AddressBook::getIsDefault,0).eq(AddressBook::getUserId,BaseContext.getCurrentId());
        deleteWrapper.eq(AddressBook::getId,id);
        baseMapper.delete(deleteWrapper);
        return CommonResult.success("删除成功");
    }

    @Override
    public CommonResult<String> updateAddress(AddressBook addressBook) {
        baseMapper.updateById(addressBook);
        return CommonResult.success("修改成功");
    }

    @Override
    public CommonResult<AddressBook> defaultAddress(AddressBook addressBook) {
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.set(AddressBook::getIsDefault, 0);
        baseMapper.update(null,wrapper);
        addressBook.setIsDefault(1);
        baseMapper.updateById(addressBook);
        return CommonResult.success(addressBook);
    }

    @Override
    public CommonResult<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> getDefaultAddressBook = new LambdaQueryWrapper<>();
        getDefaultAddressBook.eq(AddressBook::getIsDefault,1);
        getDefaultAddressBook.eq(AddressBook::getUserId,BaseContext.getCurrentId());
        return CommonResult.success(baseMapper.selectOne(getDefaultAddressBook));
    }
}
