package com.dubaiza.clean.demo.di

import android.content.Context
import androidx.room.Room
import com.dubaiza.clean.demo.data.local.dao.EmployeeDao
import com.dubaiza.clean.demo.data.local.database.AppDatabase
import com.dubaiza.clean.demo.data.repository.EmployeeRepositoryImpl
import com.dubaiza.clean.demo.domain.repository.EmployeeRepository
import com.dubaiza.clean.demo.domain.usecase.DeleteEmployeeUsecase
import com.dubaiza.clean.demo.domain.usecase.GetAllEmployeeUseCase
import com.dubaiza.clean.demo.domain.usecase.InsertEmployeeUsecase
import com.dubaiza.clean.demo.domain.usecase.InsertEmployeesUsecase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object EmployeeModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "employee_db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideEmployeeDao(db: AppDatabase): EmployeeDao {
        return db.employeeDao()
    }

    @Provides
    @Singleton
    fun provideEmployeeRepository(employeeDao: EmployeeDao): EmployeeRepository {
        return EmployeeRepositoryImpl(employeeDao)
    }

    // Provide Use Cases
    @Provides
    @Singleton
    fun provideGetAllEmployeeUseCase(repository: EmployeeRepository): GetAllEmployeeUseCase {
        return GetAllEmployeeUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideInsertEmployeeUseCase(repository: EmployeeRepository): InsertEmployeeUsecase {
        return InsertEmployeeUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideInsertEmployeesUseCase(repository: EmployeeRepository): InsertEmployeesUsecase {
        return InsertEmployeesUsecase(repository)
    }

    @Provides
    @Singleton
    fun provideDeleteEmployeeUseCase(repository: EmployeeRepository): DeleteEmployeeUsecase {
        return DeleteEmployeeUsecase(repository)
    }
}