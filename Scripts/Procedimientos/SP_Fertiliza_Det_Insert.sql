USE [ShellPest]
GO
/****** Object:  StoredProcedure [dbo].[SP_BSC_ClienteGeneral]    Script Date: 25/08/2018 12:40:29 p. m. ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
IF  EXISTS (SELECT * FROM SYS.OBJECTS WHERE TYPE = 'P' AND NAME = 'SP_Fertiliza_Det_Insert')
DROP PROCEDURE SP_Fertiliza_Det_Insert
GO
-- =============================================
-- Author:		<Author,,Name>
-- Create date: <Create Date,,>
-- Description:	<Description,,>
-- =============================================
create PROCEDURE [dbo].[SP_Fertiliza_Det_Insert] 
	-- Add the parameters for the stored procedure here
	@Id_Fertiliza char(10),
	@Fecha char(8),
	@c_codigo_pro varchar(15),
	@Cantidad_Aplicada numeric(18,2),
	@Id_Usuario varchar(10),
	@F_Usuario_Crea varchar(10),
	@c_codigo_eps char(2)
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON;

    -- Insert statements for procedure here
	declare @correcto bit

	begin transaction T1;
	begin try


		declare @Existe int
			select @Existe = count(Id_Fertiliza) from dbo.t_Fertiliza_Det a where (a.Id_Fertiliza=@Id_Fertiliza and a.c_codigo_pro=@c_codigo_pro and  Fecha=@Fecha and c_codigo_eps=@c_codigo_eps)

			if @Existe>0 
				insert into t_Fertiliza_Det (Id_Fertiliza
		           ,Fecha
		           ,c_codigo_pro
				   ,Cantidad_Aplicada
				   ,Id_Usuario_Crea
	           	   ,F_Usuario_Crea
				   ,c_codigo_eps)
				  (select Id_Fertiliza
		           ,Fecha
		           ,c_codigo_pro
				   ,Cantidad_Aplicada
				   ,Id_Usuario_Crea
	           	   ,F_Usuario_Crea,c_codigo_eps from t_Fertiliza_Det where Id_Fertiliza='-0')
			else
				INSERT INTO dbo.t_Fertiliza_Det
		           (Id_Fertiliza
		           ,Fecha
		           ,c_codigo_pro
				   ,Cantidad_Aplicada
				   ,Id_Usuario_Crea
	           	   ,F_Usuario_Crea
				   ,c_codigo_eps)
		     	VALUES
		           (@Id_Fertiliza
		           ,@Fecha
				   ,@c_codigo_pro
				   ,@Cantidad_Aplicada
		           ,@Id_Usuario
	           	   ,@F_Usuario_Crea
				   ,@c_codigo_eps)
		
		commit transaction T1;
		set @correcto=1
	end try
	begin catch
		rollback transaction T1;
		set @correcto=0
	end catch

	select @correcto resultado
END
